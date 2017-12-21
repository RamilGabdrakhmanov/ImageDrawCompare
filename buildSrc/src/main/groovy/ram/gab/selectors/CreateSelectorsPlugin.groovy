package ram.gab.selectors

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
        project.tasks.create(group: tasksGroup, name: "createSelectors", type: CreateSelectorsTask) {}
    }
}