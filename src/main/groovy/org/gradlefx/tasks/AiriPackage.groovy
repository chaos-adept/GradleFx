package org.gradlefx.tasks

import org.gradlefx.cli.CompilerOption
import org.gradlefx.conventions.FlexType

/**
 * @author <a href="mailto:drykovanov@wiley.ru">Denis Rykovanov</a>
 * $Date:  30.11.12 16:29 $
 */
class AiriPackage extends AbstractAirPackage {

    public AiriPackage() {
        description = 'Packages the generated swf file into an .airi package'
        dependsOn Tasks.COMPILE_TASK_NAME
    }

    @Override
    def protected List createCompilerArguments() {
        List airOptions = [CompilerOption.PREPARE]

        airOptions.addAll([
                "${project.buildDir}/${flexConvention.output}",
                project.relativePath(flexConvention.air.applicationDescriptor),
        ])
        flexConvention.air.includeFileTrees.add(
                project.fileTree(dir:"${project.projectDir}",
                        include:project.relativePath("${project.buildDir}/${flexConvention.output}.${FlexType.swf}")));

        addFileOptions airOptions

        return airOptions
    }



}
