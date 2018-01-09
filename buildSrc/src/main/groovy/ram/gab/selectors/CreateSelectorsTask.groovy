package ram.gab.selectors

import groovy.io.FileType
import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by ivan.puzyrev on 04.10.2016.
 */
class CreateSelectorsTask extends DefaultTask {

    final static String PREFIX = "selector"
    final static String FILE_NAME = PREFIX + "_{1}[a-z_]+_{1}[a-f0-9]{8}_{1}[a-f0-9]{8}\\.xml"
    final static String COLOR = "[a-f0-9]{8}"


    @InputDirectory
    def File inputDir

    @OutputDirectory
    def File outputDir

    @TaskAction
    void execute(IncrementalTaskInputs inputs) {
        def startTime = System.currentTimeMillis()
        println inputs.incremental ? 'CHANGED inputs considered out of date'
                : 'ALL inputs considered out of date'
        if (!inputs.incremental)
            project.delete(outputDir.listFiles())

        inputs.outOfDate { change ->
            println "out of date: ${change.file.name}"
            createSelectorForSingleFile(change.file)
        }

        inputs.removed { change ->
            println "removed: ${change.file.name}"
        }

        def time = System.currentTimeMillis() - startTime
        logger.warn("Created in " + time + " milliseconds")
    }

    def createSelectorForDir(File drawableDir) {
        drawableDir.eachFileMatch(FileType.FILES, ~/selector_cake_ffff0000_ff00ff00.xml/) { file ->
            createSelectorForSingleFile(file)
        }
    }

    def createSelectorForSingleFile(File vectorDrawable) {
        if (vectorDrawable.name.matches(FILE_NAME)) {
            logger.warn "Start work on " + vectorDrawable.name

            File outDir = new File(outputDir, "drawable")
            outDir.mkdirs()

            def parsed = new XmlSlurper().parse(vectorDrawable)

            def colors = vectorDrawable.name.findAll(COLOR)
            logger.warn "colors " + colors
            def normalColor = "#" + colors[0]
            def pressedColor = "#" + colors[1]

            int colorsStartPosition = getStartColorsPosition(vectorDrawable.name)
            String nameWithoutColors = vectorDrawable.name.substring(PREFIX.length(), colorsStartPosition)
            String normalOutName = "selector" + nameWithoutColors + "_normal"
            String pressedOutName = "selector" + nameWithoutColors + "_pressed"
            def normalOutFile = new File(outDir, normalOutName + ".xml")
            def pressedOutFile = new File(outDir, pressedOutName + ".xml")

            if (changePathColor(parsed, normalColor)) {
                saveToXml(parsed, normalOutFile)
            }

            if (changePathColor(parsed, pressedColor)) {
                saveToXml(parsed, pressedOutFile)
            }

            def charset = 'UTF-8'
            def outWriter = new File(outDir, "clickable" + nameWithoutColors + ".xml").newWriter(charset)
            new MarkupBuilder(outWriter).with {
                mkp.xmlDeclaration(version: '1.0', encoding: charset)
                mkp.comment('This is generated xml file')
                mkp.yield "\r\n"
                selector('xmlns:android': 'http://schemas.android.com/apk/res/android') {
                    mkp.yield "\r\n\r\n"
                    mkp.comment('Non focused states')
                    item('android:drawable': '@drawable/' + normalOutName,
                            'android:state_focused': 'false',
                            'android:state_pressed': 'false',
                            'android:state_selected': 'false')

                    item('android:drawable': '@drawable/' + pressedOutName,
                            'android:state_focused': 'false',
                            'android:state_pressed': 'false',
                            'android:state_selected': 'true')

                    mkp.yield "\r\n\r\n"
                    mkp.comment('Focused states')
                    item('android:drawable': '@drawable/' + pressedOutName,
                            'android:state_focused': 'true',
                            'android:state_pressed': 'false',
                            'android:state_selected': 'false')

                    item('android:drawable': '@drawable/' + pressedOutName,
                            'android:state_focused': 'true',
                            'android:state_pressed': 'false',
                            'android:state_selected': 'true')

                    mkp.yield "\r\n\r\n"
                    mkp.comment('Pressed')
                    item('android:drawable': '@drawable/' + pressedOutName,
                            'android:state_pressed': 'true')
                }
            }
        } else {
            logger.error "Wrong file: " + vectorDrawable.name
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

    int getStartColorsPosition(String vectorDrawableName) {
        Pattern pattern = Pattern.compile("_{1}" + COLOR + "_{1}" + COLOR + "\\.xml")
        Matcher matcher = pattern.matcher(vectorDrawableName)
        matcher.find()
        return matcher.start()
    }
}
