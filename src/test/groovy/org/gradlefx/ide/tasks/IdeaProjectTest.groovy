package org.gradlefx.ide.tasks

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static junit.framework.Assert.assertTrue

class IdeaProjectTest {

    @Test
    void should_create_iml_file() {
        given_project_name_is("AmandaHuggenkiss")
        when_I_create_project_config()
        then_an_iml_file_should_be_created_with_name("AmandaHuggenkiss.iml")
    }

    void given_project_name_is(String projectname) {
        File projectDir = new File(this.getClass().getResource("/stub-project-dir/intellij-dummy.xml").toURI())
        this.project = ProjectBuilder.builder().withProjectDir(projectDir.getParentFile()).withName(projectname).build()
    }

    void when_I_create_project_config() {
        IdeaProject ideaFxProjectTask = project.tasks.add("ideafx", IdeaProject)
        ideaFxProjectTask.createProjectConfig();
    }

    void then_an_iml_file_should_be_created_with_name(String filename) {
        File imlFile = project.file(filename)
        assertTrue(String.format("Iml-file %s was not created!", imlFile.absolutePath), imlFile.exists())
    }

    Project project
}
