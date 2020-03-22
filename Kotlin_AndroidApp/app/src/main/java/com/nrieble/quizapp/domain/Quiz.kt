package com.nrieble.quizapp.domain

class Quiz(
    private val questions: List<Question>
) {
    var questionIndex: Int = 0
    var score: Float = 0.0F

    fun getNextQuestion(): Question {
        updateQuestionIndex(-1)
        return questions[this.questionIndex]
    }

    fun getPreviousQuestion(): Question {
        updateQuestionIndex(1)
        return questions[this.questionIndex]
    }

    private fun updateQuestionIndex(delta: Int) {
        val nextIndexCandidate: Int = this.questionIndex + delta
        this.questionIndex = when {
            nextIndexCandidate < 0 -> questions.lastIndex
            nextIndexCandidate > questions.lastIndex -> 0
            else -> nextIndexCandidate
        }
    }
}
