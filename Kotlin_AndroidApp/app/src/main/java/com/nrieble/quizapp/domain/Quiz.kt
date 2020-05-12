package com.nrieble.quizapp.domain

import androidx.lifecycle.LiveData

class Quiz(
    val quizItems: List<QuizItem>
) : LiveData<QuizItem>() {
    var state = QuizState()
    val size = quizItems.size

    fun answer(item: QuizItem) {
        this.state.score += item.score()
        item.state = QuizItem.AnswerState.REVIEW
    }

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
}

class QuizState {
    var questionIndex: Int = 0
    var score: Float = 0.0F
}

enum class QuizType {
    TEST,
    PRACTICE
}