package ram.gab.selectors

import com.android.build.gradle.BaseExtension
import groovy.io.FileType
import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by ivan.puzyrev on 04.10.2016.
 */
class CreateSelectorsTask extends DefaultTask {

    @TaskAction
    def createSelecttorsTask() {
//        BaseExtension android = project.android
//        iterateOverResDirs(project.name, android.sourceSets.main.res.srcDirs)


        fillLocalMapsFromDir("lol", new File("/Users/ramil.gabdrakhmanov/StudioProjects/ImageDrawCompare/app/vd_icons"))
    }

    def iterateOverResDirs(String projectName, Set<File> resDirs) {
        resDirs.each { File file ->
            logger.warn "Resource dir: " + file.path
            def drawableDir = new File(file, 'drawable')
            if (drawableDir.exists()) {
                fillLocalMapsFromDir(projectName, drawableDir)
            }
        }
    }

    def fillLocalMapsFromDir(String projectName, File drawableDir) {
        drawableDir.eachFileMatch(FileType.FILES, ~/selector_cake_ffff0000_ff00ff00.xml/) { file ->
            logger.warn projectName + "Drawable xml: " + file.path


            File outDir = new File(Utils.getOutDir(project), "drawable")
            outDir.mkdirs()

            def parsed = new XmlSlurper().parse(file)

            def normalColor = "#FFff0000"
            def pressedColor = "#FF00ff00"

            def normalOutFile = new File(outDir, "selector_cake_normal.xml")
            def pressedOutFile = new File(outDir, "selector_cake_pressed.xml")

            if (changePathColor(parsed, normalColor)) {
                saveToXml(parsed, normalOutFile)
            }

            if (changePathColor(parsed, pressedColor)) {
                saveToXml(parsed, pressedOutFile)
            }

            def charset = 'UTF-8'
            def outWriter = new File(outDir, "selector_cake_ffff0000_ff00ff00.xml").newWriter(charset)
            new MarkupBuilder(outWriter).with {
                mkp.xmlDeclaration(version: '1.0', encoding: charset)
                mkp.comment('This is generated xml file')
                mkp.yield "\r\n"
                selector('xmlns:android': 'http://schemas.android.com/apk/res/android') {
                    mkp.yield "\r\n\r\n"
                    mkp.comment('Non focused states')
                    item('android:drawable': '@drawable/selector_cake_normal',
                            'android:state_focused': 'false',
                            'android:state_pressed': 'false',
                            'android:state_selected': 'false')

                    item('android:drawable': '@drawable/selector_cake_pressed',
                            'android:state_focused': 'false',
                            'android:state_pressed': 'false',
                            'android:state_selected': 'true')

                    mkp.yield "\r\n\r\n"
                    mkp.comment('Focused states')
                    item('android:drawable': '@drawable/selector_cake_pressed',
                            'android:state_focused': 'true',
                            'android:state_pressed': 'false',
                            'android:state_selected': 'false')

                    item('android:drawable': '@drawable/selector_cake_pressed',
                            'android:state_focused': 'true',
                            'android:state_pressed': 'false',
                            'android:state_selected': 'true')

                    mkp.yield "\r\n\r\n"
                    mkp.comment('Pressed')
                    item('android:drawable': '@drawable/selector_cake_pressed',
                            'android:state_pressed': 'true')
                }
            }

            logger.warn 'OK!'
        }
    }

    boolean changePathColor(GPathResult drawable, String color) {
        boolean colorChanged = false
        drawable.path.each {
            if (it.'@android:fillColor' != null) {
                it.'@android:fillColor' = color
                colorChanged = true
            }
        }

        return colorChanged
    }

    def saveToXml(GPathResult drawable, File outFile) {
        def outBuilder = new StreamingMarkupBuilder()
        def outWriter = outFile.newWriter()
        XmlUtil.serialize(outBuilder.bind { mkp.yield drawable }, outWriter)
    }
}
