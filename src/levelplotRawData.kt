import jetbrains.letsPlot.geom.geomTile
import jetbrains.letsPlot.ggplot
import jetbrains.letsPlot.scale.scaleFillGradient
import jetbrains.letsPlot.scale.scaleXContinuous
import jetbrains.letsPlot.scale.scaleYContinuous

fun levelplotRawData(
    x: YourDataType,
    xlab: String = "Length [m]",
    ylab: String = "Depth [m]",
    main: String = "${x.title} without topography (raw data)",
    col: List<String> = colors,
    trafo: (Double) -> Double = { Math.log(it) },
    aspect: String = "iso",
    vararg options: Pair<String, Any>
) {
    val data = mapOf(
        "val" to x.rawData.points.val.map(trafo),
        "dist" to x.rawData.points.dist.map { it * (-1 * x.rawData.points.depth) }
    )

    val plot = ggplot(data) +
            geomTile {
                x = "dist"
                y = "val"
                fill = "val"
            } +
            scaleFillGradient(low = col.first(), high = col.last()) +
            scaleXContinuous(name = xlab) +
            scaleYContinuous(name = ylab) +
            ggtitle(main) +
            theme(aspectRatio = aspect) +
            options.toSpec()

    plot.show()
}


