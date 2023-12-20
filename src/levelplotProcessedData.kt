import java.awt.Color
import javax.swing.JFrame
import smile.plot.swing.PlotCanvas
import smile.plot.swing.PlotPanel
import smile.plot.swing.PlotFrame
import smile.plot.level.levelplot
import smile.plot.level.ColorPalette
import smile.plot.level.ColorPalette.jet

fun levelplotProcessedData(
    x: Any,
    xlab: String = "Length [m]",
    ylab: String = "Depth [m]",
    main: String = "${x.title} without topography",
    col: Array<Color> = jet(18),
    breaks: Int = 18,
    trafo: (Double) -> Double = { Math.log(it) },
    backtrafo: (Double) -> Double = { Math.exp(it) },
    aspect: String = "iso",
    vararg args: Any
): PlotCanvas {
    val labBreaks = backtrafo(
        (trafo(x.processedData.pointsWithTopo.val.min())..trafo(x.processedData.pointsWithTopo.val.max()))
            .step((x.processedData.pointsWithTopo.val.max() - x.processedData.pointsWithTopo.val.min()) / breaks)
            .toList()
    ).map { it.roundToInt() }

    val plot = levelplot(
        { trafo(x.processedData.points.val) },
        { x.processedData.points.dist * x.processedData.points.depth },
        colorPalette = ColorPalette(col),
        xlab = xlab,
        ylab = ylab,
        main = main,
        aspect = aspect,
        colorkey = mapOf(
            "at" to (0 until breaks).map { it.toDouble() },
            "labels" to labBreaks.map { it.toString() }
        ),
        *args
    )

    return PlotCanvas(plot)
}

fun levelplotProcessedDataWithTopo(
    x: Any,
    xlab: String = "Length [m]",
    ylab: String = "Height [m]",
    main: String = "${x.title} with topography",
    col: Array<Color> = jet(18),
    breaks: Int = 18,
    trafo: (Double) -> Double = { Math.log(it) },
    backtrafo: (Double) -> Double = { Math.exp(it) },
    aspect: String = "iso",
    vararg args: Any
): PlotCanvas {
    val labBreaks = backtrafo(
        (trafo(x.processedData.pointsWithTopo.val.min())..trafo(x.processedData.pointsWithTopo.val.max()))
            .step((x.processedData.pointsWithTopo.val.max() - x.processedData.pointsWithTopo.val.min()) / breaks)
            .toList()
    ).map { it.roundToInt() }

    val plot = levelplot(
        { trafo(x.processedData.pointsWithTopo.val) },
        { round(x.processedData.pointsWithTopo.dist) * round(x.processedData.pointsWithTopo.height) },
        colorPalette = ColorPalette(col),
        xlab = xlab,
        ylab = ylab,
        main = main,
        aspect = aspect,
        colorkey = mapOf(
            "at" to (0 until breaks).map { it.toDouble() },
            "labels" to labBreaks.map { it.toString() }
        ),
        *args
    )

    return PlotCanvas(plot)
}


