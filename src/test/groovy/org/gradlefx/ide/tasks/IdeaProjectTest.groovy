package org.gradlefx.ide.tasks

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.conventions.GradleFxConvention
import org.junit.Before
import org.junit.Test

import static junit.framework.Assert.assertTrue
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.core.StringContains.containsString

class IdeaProjectTest {

    @Test
    void should_create_iml_file() {
        given_project_name_is("AmandaHuggenkiss")
        when_I_create_project_config()
        then_an_iml_file_should_be_created_with_name("AmandaHuggenkiss.iml")
    }

    @Test
    void should_create_top_xml_tags() {
        given_project_name_is("AmandaHuggenkiss")
        when_I_create_project_config()
        then_the_iml_file_should_have_tag('<module type="Flex" version="4">')
    }

    @Test
    void should_create_correct_target_player_version() {
        given_project_name_is("AmandaHuggenkiss")
        given_player_version_is("12")
        when_I_create_project_config()
        then_the_iml_file_should_have_tag('<option name="TARGET_PLAYER_VERSION" value="12" />')
    }

    void given_project_name_is(String projectname) {
        File projectDir = new File(this.getClass().getResource("/stub-project-dir/intellij-dummy.xml").toURI())
        this.project = ProjectBuilder.builder().withProjectDir(projectDir.getParentFile()).withName(projectname).build()
    }

    void given_player_version_is(String version) {
        ideaProjectTask().flexConvention.playerVersion = version
    }

    void when_I_create_project_config() {
        ideaProjectTask().createProjectConfig();
    }

    void then_an_iml_file_should_be_created_with_name(String filename) {
        File imlFile = project.file(filename)
        assertTrue(String.format("Iml-file %s was not created!", imlFile.absolutePath), imlFile.exists())
    }

    void then_the_iml_file_should_have_tag(String tag) {
        if (this.imlFileContent == null) {
            imlFileContent = project.file(project.name + ".iml").text
        }
        assertTrue(String.format("Could not find %s in %s", tag, imlFileContent), imlFileContent.contains(tag));
    }

    IdeaProject ideaProjectTask() {
        if (_ideaFxProjectTask == null) {
            _ideaFxProjectTask = project.tasks.add("ideafx", IdeaProject)
            GradleFxConvention pluginConvention = new GradleFxConvention(project)
            _ideaFxProjectTask.flexConvention = pluginConvention
            _ideaFxProjectTask.flexConvention.playerVersion = "11.5"
        }
        return _ideaFxProjectTask;
    }

    Project project
    IdeaProject _ideaFxProjectTask
    String imlFileContent
}
