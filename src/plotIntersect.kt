import java.awt.Color

fun plotIntersect(
    .Object1: ProfileSet,
    .Object2: ProfileSet? = null,
    xlab: String = "Height above sea level [m]",
    ylab: String = "Resistivity [Ωm]",
    main: String = "",
    trafo: (Double) -> Double = { Math.log(it) },
    backtrafo: (Double) -> Double = { Math.exp(it) },
    col: List<Color> = listOf(Color.RED, Color.BLUE),
    pch: List<Int> = listOf(20, 20),
    type: String = "p",
    legendLoc: String = "bottomleft"
) {
    TODO()
}

fun plotIntersect(
    .Object1: ProfileSet,
    xlab: String,
    ylab: String,
    main: String,
    trafo: (Double) -> Double,
    backtrafo: (Double) -> Double,
    col: List<Color>,
    pch: List<Int>,
    type: String,
    legendLoc: String
) {
    for (i in 0 until (.Object1.profiles.size - 1)) {
        for (j in (i + 1) until .Object1.profiles.size) {
            plotIntersect(
                .Object1.profiles[i],
            .Object1.profiles[j],
            xlab,
            ylab,
            main,
            trafo,
            backtrafo,
            col,
            pch,
            type,
            legendLoc
            )
        }
    }
}

fun plotIntersect(
    .Object1: Profile,
    .Object2: Profile,
    xlab: String,
    ylab: String,
    main: String,
    trafo: (Double) -> Double,
    backtrafo: (Double) -> Double,
    col: List<Color>,
    pch: List<Int>,
    type: String,
    legendLoc: String
) {
    val m1 = .Object1.gpsCoordinates.lmRelative.coefficients[1]
    val m2 = .Object2.gpsCoordinates.lmRelative.coefficients[1]

    val n1 = .Object1.gpsCoordinates.lmRelative.coefficients[0]
    val n2 = .Object2.gpsCoordinates.lmRelative.coefficients[0]

    val xIntersect = (n2 - n1) / (m1 - m2)
    val yIntersect = m1 * xIntersect + n1

    val xStart1 = .Object1.gpsCoordinates.relative.lon.minOrNull()!!
    val yStart1 = m1 * xStart1 + n1
    val xStart2 = .Object2.gpsCoordinates.relative.lon.minOrNull()!!
    val yStart2 = m2 * xStart2 + n2

    val xDiff1 = xIntersect - xStart1
    val yDiff1 = yIntersect - yStart1
    val xDiff2 = xIntersect - xStart2
    val yDiff2 = yIntersect - yStart2
    val length1 = Math.sqrt(xDiff1.pow(2) + yDiff1.pow(2))
    val length2 = Math.sqrt(xDiff2.pow(2) + yDiff2.pow(2))

    val indices1 = listOf(
        .Object1.processedData.pointsWithTopo.dist
            .mapIndexedNotNull { index, dist ->
        if (Math.round(dist) == Math.round(length1) ||
            Math.round(dist) == Math.round(length1 + 1) ||
            Math.round(dist) == Math.round(length1 - 1)
        ) index else null
    }
    ).flatten()
    val indices2 = listOf(
        .Object2.processedData.pointsWithTopo.dist
            .mapIndexedNotNull { index, dist ->
        if (Math.round(dist) == Math.round(length2.toDouble()) ||
            Math.round(dist) == Math.round(length2 + 1) ||
            Math.round(dist) == Math.round(length2 - 1)
        ) index else null
    }
    ).flatten()

    if (indices1.isEmpty() || indices2.isEmpty()) {
        println("No intersection between ${.Object1.title} and ${.Object2.title}.")
        return
    }

    val res1 = indices1.map { index ->
        mapOf(
            "dist" to .Object1.processedData.pointsWithTopo.dist[index],
            "height" to .Object1.processedData.pointsWithTopo.height[index],
            "val" to .Object1.processedData.pointsWithTopo.val[index]
        )
    }
    val res2 = indices2.map { index ->
        mapOf(
            "dist" to .Object2.processedData.pointsWithTopo.dist[index],
            "height" to .Object2.processedData.pointsWithTopo.height[index],
            "val" to .Object2.processedData.pointsWithTopo.val[index]
        )
    }

    val labBreaks = (trafo(minOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double })))
        .rangeTo(trafo(maxOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double }))))
    .step((trafo(maxOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double })))
            - trafo(minOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double }))) / 6)
    .map { backtrafo(it) }
        .toList()
    val atBreaks = (trafo(minOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double })))
        .rangeTo(trafo(maxOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double }))))
    .step((trafo(maxOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double })))
            - trafo(minOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double }))) / 6)
    .toList()

    plot(
        res1.map { it["height"] as Double },
        res1.map { trafo(it["val"] as Double) },
        xlim = listOf(
            minOf(res1.map { it["height"] as Double }, res2.map { it["height"] as Double }),
            maxOf(res1.map { it["height"] as Double }, res2.map { it["height"] as Double })
        ),
        ylim = listOf(
            trafo(minOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double })),
            trafo(maxOf(res1.map { it["val"] as Double }, res2.map { it["val"] as Double }))
        ),
        xlab = xlab,
        ylab = ylab,
        main = main,
        col = col[0],
        pch = pch[0],
        type = type,
        yaxt = "n"
    )
    points(
        res2.map { it["height"] as Double },
        res2.map { trafo(it["val"] as Double) },
        col = col[1],
        pch = pch[1],
        type = type
    )
    legend(
        legendLoc,
        col = col,
        pch = pch,
        legend = listOf(.Object1.title, .Object2.title)
    )
    axis(
        side = 2,
        at = atBreaks,
        labels = labBreaks
    )
}


