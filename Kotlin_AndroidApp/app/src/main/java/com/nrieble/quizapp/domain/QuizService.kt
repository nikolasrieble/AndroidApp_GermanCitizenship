package com.nrieble.quizapp.domain

import com.nrieble.quizapp.persistence.Answer
import com.nrieble.quizapp.persistence.AnswerDAO
import com.nrieble.quizapp.persistence.Question
import com.nrieble.quizapp.persistence.QuestionDAO

class QuizService(
    private val questionDAO: QuestionDAO,
    private val answerDAO: AnswerDAO
) {
    suspend fun getCategories(): List<String?> =
        questionDAO.getAll().map { it.category }

    suspend fun getPractice(): Quiz {
        val questions = questionDAO.getAll()
        val answers = answerDAO.getAll()
        return Quiz(generateQuizItems(questions, answers))
    }

    suspend fun getTest(category: String): Quiz {
        val questions = questionDAO.getAll()
        val answers = answerDAO.getAll()
        // select 27 questions from the main category
        val mainQuestions = questions.filter { it.category == "Allgemein" }.shuffled().take(27)
        // select 3 from the specified category
        val stateQuestions = questions.filter { it.category == category }.shuffled().take(3)
        return Quiz(generateQuizItems(mainQuestions + stateQuestions, answers))
    }

    private fun generateQuizItems(
        questions: List<Question>,
        answers: List<Answer>
    ): List<QuizItem> =
        questions.map { question ->
            QuizItem(
                question,
                answers.filter { it.questionId == question.id })
        }
}
