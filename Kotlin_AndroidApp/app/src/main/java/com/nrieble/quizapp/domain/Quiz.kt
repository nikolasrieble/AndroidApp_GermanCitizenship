package com.nrieble.quizapp.domain

class Quiz(
    private val questions: List<Question>
) {
    val size = questions.size
    var progress = QuizProgress()

    fun getNextQuestion(): Question {
        updateQuestionIndex(1)
        return questions[this.progress.questionIndex]
    }

    fun getPreviousQuestion(): Question {
        updateQuestionIndex(-1)
        return questions[this.progress.questionIndex]
    }

    private fun updateQuestionIndex(delta: Int) {
        val nextIndexCandidate: Int = this.progress.questionIndex + delta
        this.progress.questionIndex = when {
            nextIndexCandidate < 0 -> questions.lastIndex
            nextIndexCandidate > questions.lastIndex -> 0
            else -> nextIndexCandidate
        }
    }

    fun answer(question: Question) {
        this.progress.score += question.score()
        question.state = Question.AnswerState.REVIEW
    }

    class QuizProgress {
        var questionIndex: Int = 0
        var score: Float = 0.0F
    }
}
