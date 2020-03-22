package com.nrieble.quizapp

class Question(
    val question: String,
    val options: List<String>,
    val truth: List<Boolean>,
    val id: Int,
    val image: String,
    val category: String
) {
    var selection = BooleanArray(options.size).toMutableList()
    var disclosure = false
    var answered = false

    fun score(): Float {
        if (this.truth == this.selection) {
            return 1.0F
        }
        return 0.0F
    }
}
