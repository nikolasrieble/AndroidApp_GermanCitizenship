package com.nrieble.quizapp.domain

class Quiz(
    private val quizItems: List<QuizItem>
) {
    val size = quizItems.size
    var state = QuizState()

    fun getNextQuestion(): QuizItem {
        updateQuestionIndex(1)
        return quizItems[this.state.questionIndex]
    }

    fun getPreviousQuestion(): QuizItem {
        updateQuestionIndex(-1)
        return quizItems[this.state.questionIndex]
    }

    private fun updateQuestionIndex(delta: Int) {
        val nextIndexCandidate: Int = this.state.questionIndex + delta
        this.state.questionIndex = when {
            nextIndexCandidate < 0 -> quizItems.lastIndex
            nextIndexCandidate > quizItems.lastIndex -> 0
            else -> nextIndexCandidate
        }
    }

    fun answer(quizItem: QuizItem) {
        this.state.score += quizItem.score()
        quizItem.state = QuizItem.AnswerState.REVIEW
    }

    class QuizState {
        var questionIndex: Int = 0
        var score: Float = 0.0F
    }
}
