/*
 * Copyright (c) 2011 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradlefx.tasks

import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTreeElement
import org.gradlefx.cli.CompilerOption;
import org.gradlefx.conventions.FlexType;

class AirPackage extends AbstractAirPackage {
    
    public AirPackage() {
        super();
    }

    @Override
    def protected List createCompilerArguments() {
        List airOptions = [CompilerOption.PACKAGE]

        addAirSigningOptions airOptions

        airOptions.addAll([
            project.file(project.buildDir.name + '/' + flexConvention.output).absolutePath,
            project.relativePath(flexConvention.air.applicationDescriptor),
            project.relativePath("${project.buildDir}/${flexConvention.output}.${FlexType.swf}")
        ])

        addFiles(airOptions)

        return airOptions
    }

    private void addFiles(List compilerOptions) {
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
