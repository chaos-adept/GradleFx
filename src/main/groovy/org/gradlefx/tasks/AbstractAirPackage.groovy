package org.gradlefx.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradlefx.conventions.GradleFxConvention
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement

/**
 * @author <a href="mailto:drykovanov@wiley.ru">Denis Rykovanov</a>
 * $Date:  03.12.12 14:35 $
 */
abstract class AbstractAirPackage extends DefaultTask {

    private static final String ANT_RESULT_PROPERTY = 'airPackageResult'
    private static final String ANT_OUTPUT_PROPERTY = 'airPackageOutput'

    GradleFxConvention flexConvention;

    public AbstractAirPackage() {
        flexConvention = (GradleFxConvention) project.convention.plugins.flex

        dependsOn Tasks.COMPILE_TASK_NAME
    }

    @TaskAction
    def packageApk() {
        validate();

        List compilerArguments = createCompilerArguments()

        ant.java(jar: flexConvention.flexHome + '/lib/adt.jar',
                fork: true,
                resultproperty: ANT_RESULT_PROPERTY,
                outputproperty: ANT_OUTPUT_PROPERTY) {

            compilerArguments.each { compilerArgument ->
                arg(value: compilerArgument)
            }
        }

        handlePackageIfFailed ANT_RESULT_PROPERTY, ANT_OUTPUT_PROPERTY

        showAntOutput ant.properties[ANT_OUTPUT_PROPERTY]
    }


    protected void addAirSigningOptions(List compilerOptions) { //todo extract to utility
        compilerOptions.addAll ([
                "-storetype",
                "pkcs12",
                "-keystore",
                flexConvention.air.keystore,
                "-storepass",
                flexConvention.air.storepass
        ])
    }

    def protected abstract List createCompilerArguments();

    protected void addFileOptions(List compilerOptions) { //todo extract to utility
        flexConvention.air.includeFileTrees.each { ConfigurableFileTree fileTree ->
            compilerOptions.add "-C"
            compilerOptions.add fileTree.dir.absolutePath

            fileTree.visit { FileTreeElement file ->
                if (!file.isDirectory()) {
                    compilerOptions.add file.relativePath
                }
            }
        }
    }

}
