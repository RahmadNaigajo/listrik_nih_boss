interface AdjustHeight {
    fun adjustHeight(delta: Int)
}

class Profile : AdjustHeight {
    var height: Int = 0

    override fun adjustHeight(delta: Int) {
        height += delta
    }
}

fun main() {
    val profile = Profile()
    profile.adjustHeight(10)
}


