import java.awt.Color
import java.util.logging.Level
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import smile.plot.Palette
import smile.plot.PlotCanvas
import smile.plot.PlotPanel
import smile.plot.levelplot
import smile.plot.legendLabel

fun levelplot(x: Profile, dataType: String = "processed", withTopo: Boolean = false,
              xlab: String = "Length [m]", ylab: String = "Depth [m]", main: String = x.title,
              col: Array<Color> = Palette.jet(18), breaks: Int = 18, trafo: (Double) -> Double = Math::log,
              backtrafo: (Double) -> Double = Math::exp, aspect: String = "iso", vararg options: Any) {
    if (dataType == "processed") {
        if (withTopo) {
            levelplotProcessedDataWithTopo(x, xlab, ylab, main, col, breaks, trafo, backtrafo, aspect, *options)
        } else {
            levelplotProcessedData(x, xlab, ylab, main, col, breaks, trafo, backtrafo, aspect, *options)
        }
    } else {
        levelplotRawData(x, xlab, ylab, main, col, trafo, aspect, *options)
    }
}

fun levelplot(x: ProfileSet, dataType: String = "processed", withTopo: Boolean = false,
              xlab: String = "Length [m]", ylab: String = "Depth [m]", main: String = x.title,
              col: Array<Color> = Palette.jet(18), breaks: Int = 18, trafo: (Double) -> Double = Math::log,
              backtrafo: (Double) -> Double = Math::exp, aspect: String = "iso", vararg options: Any) {
    x.profiles.forEach { profile ->
        levelplot(profile, dataType, withTopo, xlab, ylab, main, col, breaks, trafo, backtrafo, aspect, *options)
    }
}

fun levelplotLegendLabel(legendLab: String = "Resistivity", unit: String = "[Ωm]") {
    PlotPanel.legendLabel = legendLabel(legendLab, unit)
}

fun main() {
    val frame = JFrame("Level Plot")
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    val panel = JPanel()
    val canvas = PlotCanvas(panel)
    panel.add(canvas)
    frame.contentPane = panel
    frame.setSize(800, 600)
    frame.isVisible = true

    // Example usage
    val profile = Profile()
    levelplot(profile)
}


