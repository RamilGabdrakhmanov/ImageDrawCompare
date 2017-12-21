package ram.gab.selectors

import org.gradle.api.Project

class Utils {
    static String projectRelativeGeneratedResourcesPath = 'generated/res/icons'

    static File getOutDir(Project project) {
        new File(project.buildDir, projectRelativeGeneratedResourcesPath)
    }

}