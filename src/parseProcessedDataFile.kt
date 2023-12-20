import jdk.internal.net.http.frame.DataFrame
import java.io.File

fun parseProcessedDataFile(address: String, skip: Int = 0): List<DataFrame> {
    val file = File(address)
    val lines = file.readLines()
    var skipCount = skip
    var numberOfRows = 0
    var numberOfRows2 = 0
    var i = 0

    while (lines[i].contains("/")) {
        i++
        skipCount++
    }

    while (!lines[i].contains("/")) {
        i++
        numberOfRows++
    }

    var skipLines2 = skipCount + numberOfRows

    while (lines[i].contains("/")) {
        i++
        skipLines2++
    }

    while (!lines[i].contains("/")) {
        i++
        numberOfRows2++
    }

    val points = lines
        .subList(skipCount, skipCount + numberOfRows)
        .map { it.split("\\s+".toRegex()) }
        .map { row -> listOf(row[0], row[1], row[2]) }
        .toTypedArray()

    val pointsWithTopo = lines
        .subList(skipLines2, skipLines2 + numberOfRows2)
        .map { it.split("\\s+".toRegex()) }
        .map { row -> listOf(row[0], row[1], row[2]) }
        .toTypedArray()

    return listOf(
        DataFrame(points, listOf("dist", "depth", "val")),
        DataFrame(pointsWithTopo, listOf("dist", "height", "val"))
    )
}


