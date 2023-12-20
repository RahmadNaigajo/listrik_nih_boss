import jdk.internal.net.http.frame.DataFrame
import java.io.File

fun parseRawDataFile(address: String, skip: Int = 9): DataFrame {
    val file = File(address)
    val lines = file.readLines()
    var numberOfRows = 0
    for (i in 1..(1 + skip)) {
        lines[i]
    }
    var oneLine = lines[1 + skip]
    while (oneLine.isNotEmpty()) {
        oneLine = lines[numberOfRows + 1 + skip]
        numberOfRows++
    }
    val rawData = file.readLines().drop(skip).map { it.split("\\s+".toRegex()) }
    val result = DataFrame(rawData[0], rawData[1], rawData[3])
    result.colnames = listOf("dist", "depth", "val")
    return result
}
