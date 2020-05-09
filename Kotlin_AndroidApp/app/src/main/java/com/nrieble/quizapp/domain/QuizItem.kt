package com.nrieble.quizapp.domain

import androidx.lifecycle.LiveData
import com.nrieble.quizapp.database.Answer
import com.nrieble.quizapp.database.Question

class QuizItem(val question: Question,
               val answers: List<Answer>) {

    val options: List<String> = answers.map { it.text.toString() }
    val truth: List<Boolean> = answers.map { it.truth == 1 }

    var selection = BooleanArray(answers.size).toMutableList()
    var disclosure = false
    var state = AnswerState.READY

    fun score(): Float {
        if (truth == this.selection) {
            return 1.0F
        }
        return 0.0F
    }

    fun answerSelected(): Boolean {
        return this.selection.contains(true)
    }

    enum class AnswerState {
        READY,
        REVIEW
    }
}
