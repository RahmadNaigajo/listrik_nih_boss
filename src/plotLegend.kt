import java.awt.Color
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel

fun plotLegend(
    Object: Any,
    legend.lab: String = "Resistivity [Ω m]",
minData: Double = 0.0,
maxData: Double = 999999.0,
breaks: DoubleArray? = null,
legend.line: Double = 2.2,
nlevel: Int = 18,
lab.breaks: DoubleArray = doubleArrayOf(),
horizontal: Boolean = true,
col: Array<Color> = arrayOf(),
trafo: (Double) -> Double = { Math.log(it) },
backtrafo: (Double) -> Double = { Math.exp(it) },
vararg others: Any
) {
    if (lab.breaks.isNotEmpty()) {
        nlevel = lab.breaks.size
    }
    if (lab.breaks.isEmpty()) {
        lab.breaks = (trafo(minData)..trafo(maxData)).step((maxData - minData) / nlevel).map { backtrafo(it).roundToInt().toDouble() }.toDoubleArray()
    }
    val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()
    graphics.color = Color.WHITE
    graphics.fill(Rectangle2D.Float(0f, 0f, 100f, 100f))
    val legend = image.plot(
        legend.only = true,
        add = false,
        breaks = breaks,
        zlim = doubleArrayOf(minData, maxData),
        legend.line = legend.line.toFloat(),
        legend.lab = legend.lab,
        nlevel = nlevel,
        col = col.toList().toTypedArray(),
        lab.breaks = lab.breaks,
        horizontal = horizontal,
        *others
    )
    val frame = JFrame()
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.add(JLabel(ImageIcon(legend)))
    frame.pack()
    frame.isVisible = true
}

fun plotLegend(Object: ProfileSet, legend.lab: String, minData: Double = .Object.minData, maxData: Double = .Object.maxData) {
}

fun plotLegend(Object: Profile, legend.lab: String, minData: Double = .Object.processedData.minData, maxData: Double = .Object.processedData.maxData) {
}


