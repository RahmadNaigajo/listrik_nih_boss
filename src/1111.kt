import java.io.File


data class Profile(
    val title: String,
    val processedData: ProcessedData,
    val rawData: RawData,
    val measurementType: String,
    val gpsCoordinates: GpsCoordinates
)

data class ProcessedData(
    val address: String
)

data class RawData(
    val address: String
)

data class GpsCoordinates(
    val address: String
)

data class ProfileSet(
    val profiles: List<Profile>,
    val title: String
)



fun adjustHeight(profile: Profile, height: Int): Profile {
    // Adjust height logic here
    return profile
}

val p1 = Profile(
    title = "Profile 1",
    processedData = ProcessedData(
        address = "./inst/extdata/processed/p1_DipolDipol_SW-NE.xyz"),
    rawData = RawData(address = "./inst/extdata/raw/p1_DipolDipol_SW-NE.dat"),
    measurementType = "DipoleDipole",
    gpsCoordinates = GpsCoordinates(address = "./inst/extdata/gps/p1.txt")
)

val p2 = Profile(
    title = "Profile 2",
    processedData = ProcessedData(address = "./inst/extdata/processed/p2_DipolDipol_SSW-NNE.xyz"),
    rawData = RawData(address = "./inst/extdata/raw/p2_DipolDipol_SSW-NNE.dat"),
    measurementType = "DipoleDipole",
    gpsCoordinates = GpsCoordinates(address = "./inst/extdata/gps/p2.txt")
)

val p3 = Profile(
    title = "Profile 3",
    processedData = ProcessedData(address = "./inst/extdata/processed/p3_DipolDipol_S-N.xyz"),
    rawData = RawData(address = "./inst/extdata/raw/p3_DipolDipol_S-N.dat"),
    measurementType = "DipoleDipole",
    gpsCoordinates = GpsCoordinates(address = "./inst/extdata/gps/p3.txt")
)

val adjustedP3 = adjustHeight(p3, -10)

val sinkhole = ProfileSet(
    profiles = listOf(p1, p2, adjustedP3),
    title = "Sinkhole"
)

fun main(){
    p1
}