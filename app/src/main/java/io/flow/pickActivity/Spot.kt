package io.flow.pickActivity

data class Spot(
        val id: Long = counter++,
        val name: String,
        val age: String,
        val contents: String,
        val url: String
) {
    companion object {
        private var counter = 0L
    }
}
