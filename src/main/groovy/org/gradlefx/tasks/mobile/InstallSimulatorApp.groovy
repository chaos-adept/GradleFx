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
package org.gradlefx.tasks.mobile

import org.gradlefx.tasks.Tasks

/**
 * Installs the app on the simulator.
 */
class InstallSimulatorApp extends InstallApp {
    def getPlatformSdk() {
        flexConvention.airMobile.simulatorPlatformSdk
    }

    def getTargetDevice() {
        flexConvention.airMobile.simulatorTargetDevice
    }

    def getUninstallTaskName() {
        Tasks.UNINSTALL_SIMULATOR_MOBILE_TASK_NAME
    }

    def getPackageTaskName() {
        Tasks.PACKAGE_SIMULATOR_MOBILE_TASK_NAME
    }

    def getPackageOutputPath() {
        return InstallAppUtils.getSimulatorOutputPath(flexConvention, project)
    }
}
