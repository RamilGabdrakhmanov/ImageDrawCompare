package ram.gab.selectors

import com.android.build.gradle.AppExtension
import org.gradle.api.GradleScriptException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by ivan.puzyrev on 27.09.2016.
 */
class CreateSelectorsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if (!project.hasProperty('android')) {
            throw new GradleScriptException("This plugin must be applied only to android projects.")
        }
        def tasksGroup = "icons"
        project.tasks.create(group: tasksGroup, name: "createSelectors", type: CreateSelectorsTask) {
            inputDir = new File("/Users/ramil.gabdrakhmanov/StudioProjects/ImageDrawCompare/app/vd_icons")
            outputDir = Utils.getOutDir(project)
        }
        AppExtension android = project.android
        android.sourceSets.main.res.srcDir(Utils.getOutDir(project))
    }
}