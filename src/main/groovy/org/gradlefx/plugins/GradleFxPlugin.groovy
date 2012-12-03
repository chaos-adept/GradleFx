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

package org.gradlefx.plugins

import org.gradle.api.Project
import org.gradle.api.artifacts.PublishArtifact;
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.internal.artifacts.publish.DefaultPublishArtifact
import org.gradlefx.configuration.Configurations
import org.gradlefx.configuration.FlexAntTasksConfigurator

import org.gradlefx.tasks.ASDoc;
import org.gradlefx.tasks.AirPackage
import org.gradlefx.tasks.Build;
import org.gradlefx.tasks.CopyResources;
import org.gradlefx.tasks.HtmlWrapper
import org.gradlefx.tasks.Publish;
import org.gradlefx.tasks.Tasks;
import org.gradlefx.tasks.Test;
import org.gradlefx.tasks.compile.Compile
import org.gradlefx.configuration.sdk.DefaultSdkInitialisationContext
import org.gradlefx.configuration.sdk.states.flex.DetermineFlexSdkDeclarationTypeState
import org.gradlefx.configuration.sdk.states.air.DetermineAirSdkDeclarationTypeState
import org.gradlefx.tasks.CleanSdks;
import org.gradlefx.tasks.AiriPackage
import org.gradlefx.tasks.ApkPackage;

class GradleFxPlugin extends AbstractGradleFxPlugin {

    @Override
    protected void addTasks() {
        //generic tasks
        addTask Tasks.BUILD_TASK_NAME, Build
        addTask Tasks.COPY_RESOURCES_TASK_NAME, CopyResources
        addTask Tasks.PUBLISH_TASK_NAME, Publish
        addTask Tasks.TEST_TASK_NAME, Test
        addTask Tasks.COMPILE_TASK_NAME, Compile
        addTask Tasks.CLEAN_SDKS, CleanSdks

        //conditional tasks
        addTask Tasks.ASDOC_TASK_NAME, ASDoc, { flexConvention.type.isLib() }
        addTask Tasks.PACKAGE_TASK_NAME, AirPackage, { flexConvention.type.isNativeApp() }
        addTask Tasks.PACKAGE_AIRI_TASK_NAME, AiriPackage, { flexConvention.type.isNativeApp() }
        addTask Tasks.PACKAGE_APK_TASK_NAME, ApkPackage, { flexConvention.type.isNativeApp() }
        addTask Tasks.CREATE_HTML_WRAPPER, HtmlWrapper, { flexConvention.type.isWebApp() }
    }
    
    @Override
    protected void configure(Project project) {
        initializeSDKs()

        project.gradle.taskGraph.whenReady {
            if(!isCleanSdksGoingToRun()) {
                new FlexAntTasksConfigurator(project).configure()
            }
        }
        
        if (!flexConvention.type.isNativeApp())
            addArtifactsToDefaultConfiguration project
    }

    private Boolean isCleanSdksGoingToRun() {
        project.gradle.taskGraph.hasTask((CleanSdks)project[Tasks.CLEAN_SDKS]);
    }

    private void initializeSDKs() {
        new DefaultSdkInitialisationContext(project, new DetermineFlexSdkDeclarationTypeState()).initSdk()
        new DefaultSdkInitialisationContext(project, new DetermineAirSdkDeclarationTypeState()).initSdk()
    }

    /**
     * Adds artifacts to the default configuration
     * @param project
     */
    private void addArtifactsToDefaultConfiguration(Project project) {
        String type = flexConvention.type.toString()
        File artifactFile = project.file project.buildDir.name + "/" + flexConvention.output + "." + type
        PublishArtifact artifact = new DefaultPublishArtifact(project.name, type, type, null, new Date(), artifactFile)
        
        project.artifacts { ArtifactHandler artifactHandler ->
            Configurations.ARTIFACT_CONFIGURATIONS.each { Configurations configuration ->
                String configName = configuration.configName()
                artifactHandler."$configName" artifact
            }
        }
    }

}
