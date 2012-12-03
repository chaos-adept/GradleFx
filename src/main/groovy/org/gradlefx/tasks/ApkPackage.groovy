package org.gradlefx.tasks

import org.gradlefx.cli.CompilerOption

/**
 * @author <a href="mailto:drykovanov@wiley.ru">Denis Rykovanov</a>
 * $Date:  03.12.12 12:37 $
 */
class ApkPackage extends AbstractAirPackage {

    public ApkPackage() {
        super()
        description = 'Packages the generated airi package into an .apk package'
        dependsOn Tasks.PACKAGE_AIRI_TASK_NAME
    }

    @Override
    protected List createCompilerArguments() {
        List airOptions = [CompilerOption.PACKAGE, CompilerOption.TARGET]

        airOptions.add "apk"

        addAirSigningOptions airOptions

        airOptions.addAll([
                project.relativePath("${project.buildDir}/${flexConvention.output}.apk"),
                project.relativePath("${project.buildDir}/${flexConvention.output}.airi")
        ])

        return airOptions
    }

}
