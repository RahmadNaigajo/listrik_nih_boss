import kotlin.collections.*

fun plot(x: Profile,
         dataType: String = "processed",
         withTopo: Boolean = true,
         xlab: String = "Length [m]",
         ylab: String = "Height [m]",
         main: String = "${x.title} with topography",
         asp: Int = 1,
         vararg args: Any) {
    if (dataType == "processed") {
        if (withTopo) {
            plotProcessedDataWithTopo(x, xlab, ylab, main, *args, asp = asp)
        } else {
            plotProcessedData(x, xlab, ylab, main, *args, asp = asp)
        }
    } else {
        if (withTopo) {
            plotRawDataWithTopo(x, xlab, ylab, main, *args, asp = asp)
        } else {
            plotRawData(x, xlab, ylab, main, *args, asp = asp)
        }
    }
}

fun plot(x: ProfileSet,
         dataType: String = "processed",
         withTopo: Boolean = true,
         xlab: String = "Length [m]",
         ylab: String = "Height [m]",
         main: String = "${x.title} with topography",
         asp: Int = 1,
         vararg args: Any) {
    x.profiles.forEach { plot(it, dataType, withTopo, xlab, ylab, main, asp, *args) }
}


