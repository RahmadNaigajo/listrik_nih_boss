import kotlin.Double
import kotlin.collections.ArrayList
import kotlin.collections.List

interface HeightInformation {
    val dist: Double
    val height: Double
}

class ProcessedData : HeightInformation {
    override var dist: Double = 0.0
    override var height: Double = 0.0
}

fun getHeightInformation(`object`: ProcessedData): List<HeightInformation> {
    val heightInformation: ArrayList<HeightInformation> = ArrayList()
    var j = 1
    for (i in 1..`object`.pointsWithTopo[0].toInt()) {
        val indices: List<Int> = `object`.pointsWithTopo[0].mapIndexedNotNull { index, value ->
            if (Math.round(value) == i.toDouble()) index else null
        }
        if (indices.isNotEmpty()) {
            val index = indices.minOrNull()!!
            heightInformation.add(ProcessedData().apply {
                dist = `object`.pointsWithTopo[index][0]
                height = `object`.pointsWithTopo[index][1]
            })
            j++
        }
    }
    return heightInformation
}


