package com.nrieble.quizapp.domain

class Quiz(
    private val questions: List<Question>
) {
    val size = questions.size
    var state = QuizState()

    fun getNextQuestion(): Question {
        updateQuestionIndex(1)
        return questions[this.state.questionIndex]
    }

    fun getPreviousQuestion(): Question {
        updateQuestionIndex(-1)
        return questions[this.state.questionIndex]
    }

    private fun updateQuestionIndex(delta: Int) {
        val nextIndexCandidate: Int = this.state.questionIndex + delta
        this.state.questionIndex = when {
            nextIndexCandidate < 0 -> questions.lastIndex
            nextIndexCandidate > questions.lastIndex -> 0
            else -> nextIndexCandidate
        }
    }

    fun answer(question: Question) {
        this.state.score += question.score()
        question.state = Question.AnswerState.REVIEW
    }

    class QuizState {
        var questionIndex: Int = 0
        var score: Float = 0.0F
    }
}
