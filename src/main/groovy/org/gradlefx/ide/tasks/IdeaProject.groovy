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

package org.gradlefx.ide.tasks


class IdeaProject extends AbstractIDEProject {
    public static final String NAME = 'idea'
    private String imlFilename

    public IdeaProject() {
        super('IntelliJ IDEA')
    }

    @Override
    protected void invalidateConventions() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void createProjectConfig() {
        imlFilename = project.name + ".iml"
        createImlFile()
        addSourceDirs()
    }

    void createImlFile() {
        String path = "/templates/idea/template-iml.xml"
        InputStream stream = getClass().getResourceAsStream(path)

        writeContent stream, project.file(imlFilename), true
    }

    private void addSourceDirs() {
        editXmlFile imlFilename, { xml ->
            def component = xml.component.find { it.'@name' == 'NewModuleRootManager' }

            def parent = new Node(component, 'content', [url: "file://\$MODULE_DIR\$"])

            def addSrcFolder = { isTest ->
                return {
                    new Node(parent, 'sourceFolder', [
                        url: "file://\$MODULE_DIR\$/" + it,
                        isTestSource: "$isTest"
                ]) }
            };

            flexConvention.srcDirs.each addSrcFolder(false)
            flexConvention.resourceDirs.each addSrcFolder(false)

            flexConvention.testDirs.each addSrcFolder(true)
            flexConvention.testResourceDirs.each addSrcFolder(true)
        }
    }
}
