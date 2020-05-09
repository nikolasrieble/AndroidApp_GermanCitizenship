package com.nrieble.quizapp.domain

import com.nrieble.quizapp.persistence.AnswerDAO
import com.nrieble.quizapp.persistence.QuestionDAO

class QuizItemRepository(
    private val questionDAO: QuestionDAO,
    private val answerDAO: AnswerDAO
) {
    suspend fun getCategories(): List<String?> =
        questionDAO.getAll().map { it.category }

    suspend fun getQuizItems(): List<QuizItem> {
        val questions = questionDAO.getAll()
        val answers = answerDAO.getAll()

        return questions.map { question ->
            QuizItem(
                question,
                answers.filter { it.questionId == question.id })
        }
    }
}