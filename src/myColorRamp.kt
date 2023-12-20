import java.awt.Color
import java.lang.StringTemplate.interpolate

fun myColorRamp(col: Array<Color>, values: DoubleArray, minData: Double = values.minOrNull() ?: 0.0, maxData: Double = values.maxOrNull() ?: 1.0): Array<Color> {
    val v = values.map { (it - minData) / (maxData - minData) }
        .map { if (it < 0) 0.0 else if (it > 1) 1.0 else it }
        .toDoubleArray()
    val x = col.mapIndexed { index, color -> color.interpolate(col.last(), v[index]) }
    return x.toTypedArray()
}


