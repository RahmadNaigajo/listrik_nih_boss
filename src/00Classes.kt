import jdk.internal.net.http.frame.DataFrame
import java.io.File

data class RawData(
        val address: String,
        val points: DataFrame
)

fun RawData.initialize(address: String, skip: Int = 9): RawData {
    return if (address.isEmpty()) {
        println("Created an empty raw data object.")
        this
    } else if (!File(address).exists()) {
        throw Exception("Raw data file address is given but file cannot be found.")
    } else {
        this.copy(address = address, points = parseRawDataFile(address, skip))
    }
}

data class ProcessedData(
        val address: String,
        val points: DataFrame,
        val pointsWithTopo: DataFrame,
        val minData: Double,
        val maxData: Double,
        val height: DataFrame
)

fun ProcessedData.initialize(address: String, skip: Int = 0): ProcessedData {
    return if (address.isEmpty()) {
        println("Created an empty processed data object.")
        this
    } else if (!File(address).exists()) {
        throw Exception("Processed data file address is given but file cannot be found.")
    } else {
        val parsedProcessedData = parseProcessedDataFile(address, skip)
        this.copy(
                address = address,
                points = parsedProcessedData[0],
                pointsWithTopo = parsedProcessedData[1],
                minData = parsedProcessedData[1][3].min(),
                maxData = parsedProcessedData[1][3].max(),
                height = getHeightInformation(this)
        )
    }
}

data class GpsCoordinates(
        val address: String,
        val exact: DataFrame,
        val lm: LinearRegression,
        val relative: List<Pair<Double, Double>>,
        val lmRelative: LinearRegression
)

fun GpsCoordinates.initialize(address: String): GpsCoordinates {
    return if (address.isEmpty()) {
        println("Created an empty GPS coordinates object.")
        this
    } else if (!File(address).exists()) {
        throw Exception("GPS coordinates file address is given but file cannot be found.")
    } else {
        val gpsData = File(address).readText().split("\n").map { it.split(" ") }
        val exact = DataFrame(
                "lat" to gpsData.map { it[0].toDouble() },
                "lon" to gpsData.map { it[1].toDouble() }
        )
        val lm = LinearRegression.fit(exact["lat"], exact["lon"])
        val minLat = gpsData.map { it[0].toDouble() }.min()
        val minLon = gpsData.map { it[1].toDouble() }.min()
        val relativeCoords = calcRelativeCoords(this, minLat, minLon)
        val lmRelative = LinearRegression.fit(relativeCoords["lat"], relativeCoords["lon"])
        this.copy(
                address = address,
                exact = exact,
                lm = lm,
                relative = relativeCoords,
                lmRelative = lmRelative
        )
    }
}

data class Profile(
        val title: String,
        val number: Int,
        val processedData: ProcessedData,
        val rawData: RawData,
        val measurementType: String,
        val gpsCoordinates: GpsCoordinates
)

data class ProfileSet(
        val profiles: List<Profile>,
        val title: String,
        val minLat: Double,
        val minLon: Double,
        val minData: Double,
        val maxData: Double
)

fun ProfileSet.initialize(
        profiles: List<Profile> = emptyList(),
        title: String = "",
        minData: Double = 9999999.0,
        maxData: Double = 0.0,
        minLat: Double = 100000000000.0,
        minLon: Double = 100000000000.0
): ProfileSet {
    val updatedProfiles = mutableListOf<Profile>()
    for (profile in profiles) {
        val minDataX = profile.processedData.points.val.min()
        val maxDataX = profile.processedData.points.val.max()
        if (minDataX < minData) minData = minDataX
        if (maxDataX > maxData) maxData = maxDataX
        val minLatX = profile.gpsCoordinates.exact.lat.min()
        val minLonX = profile.gpsCoordinates.exact.lon.min()
        if (minLatX < minLat) minLat = minLatX
        if (minLonX < minLon) minLon = minLonX
        val relativeCoords = calcRelativeCoords(profile.gpsCoordinates, minLat, minLon)
        val lmRelative = LinearRegression.fit(relativeCoords.lat, relativeCoords.lon)
        updatedProfiles.add(
                profile.copy(
                        number = updatedProfiles.size + 1,
                        gpsCoordinates = profile.gpsCoordinates.copy(
                                relative = relativeCoords,
                                lmRelative = lmRelative
                        )
                )
        )
    }
    return ProfileSet(
            profiles = updatedProfiles,
            title = title,
            minLat = minLat,
            minLon = minLon,
            minData = minData,
            maxData = maxData
    )
}


