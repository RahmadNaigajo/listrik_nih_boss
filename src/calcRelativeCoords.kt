fun calcRelativeCoords(coords: List<Pair<Double, Double>>, minLat: Double, minLon: Double): List<Pair<Double, Double>> {
    val relativeCoords = mutableListOf<Pair<Double, Double>>()

    if (coords.maxByOrNull { it.first }?.first ?: 0.0 < 180) {
        for (coord in coords) {
            val lat = (coord.first - minLat) * 111000
            val lon = (coord.second - minLon) * 72000
            relativeCoords.add(Pair(lat, lon))
        }
    } else {
        for (coord in coords) {
            val lat = coord.first - minLat
            val lon = coord.second - minLon
            relativeCoords.add(Pair(lat, lon))
        }
    }

    return relativeCoords
}


