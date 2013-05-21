package org.gradlefx.ide.tasks

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradlefx.conventions.FrameworkLinkage
import org.gradlefx.conventions.GradleFxConvention
import spock.lang.Specification

import static junit.framework.Assert.assertTrue

/**
 * @author <a href="mailto:drykovanov@wiley.com">Denis Rykovanov</a>
 * @since 21.05.13
 */
class IdeaProjectModuleTest extends Specification {

    IdeaProject getIdeaProjectTask() {
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

    def setup() {
    }

    def "test generation empty project"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = "swc"
        when:
            ideaProjectTask.createProjectConfig()
        then:
            File imlFile = project.file("${project.name}.iml")
            imlFile.exists() == true
    }

    def "config for pure web lib"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = "swc"
            ideaProjectTask.flexConvention.frameworkLinkage = FrameworkLinkage.none
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            configuration.'@name'.text() == 'test'
            configuration.'@output-type'.text() == "Library"
            configuration.'@pure-as'.text() == "true"
    }

    def "config for flex web lib"() {
        given:
            setupProjectWithName "test"
            ideaProjectTask.flexConvention.type = "swc"
            ideaProjectTask.flexConvention.frameworkLinkage = FrameworkLinkage.external
        when:
            ideaProjectTask.createProjectConfig()
        then:
            def configuration = getModuleConfNode()
            configuration.'@name'.text() == 'test'
            configuration.'@output-type'.text() == "Library"
            configuration.'@pure-as'.text() == "false"
    }

    def setupProjectWithName(String projectName) {
        File projectDir = new File(this.getClass().getResource("/stub-project-dir/intellij-dummy.xml").toURI())
        this.project = ProjectBuilder.builder().withProjectDir(projectDir.getParentFile()).withName(projectName).build()
    }

    def getModuleConfNode() {
        File imlFile = project.file("${project.name}.iml")
        def xml = new XmlParser().parse(imlFile);

        def configManager = xml.component.find { it ->
            it.@name == "FlexBuildConfigurationManager" }
        configManager != null

        return configManager.configurations.configuration
    }

}
