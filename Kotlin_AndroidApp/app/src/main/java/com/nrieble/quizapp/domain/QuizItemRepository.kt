package com.nrieble.quizapp.domain

import android.content.Context
import androidx.room.Room
import com.nrieble.quizapp.database.*

class QuizItemRepository(
    context: Context

) {
    private val quizDatabase = Room.databaseBuilder(
        context,
        QuizDatabase::class.java,
        "quizDatabase.db"
    )
        .createFromAsset("AllGermanCitizenshipV2.db")
        .build()
    private val questionDAO: QuestionDAO = quizDatabase.questionDao()
    private val answerDAO: AnswerDAO = quizDatabase.answerDao()

    fun getCategories(): List<String?> =
        questionDAO.getAll().map { it.category }

    fun getQuizItems(): List<QuizItem> {
        val questions = questionDAO.getAll()
        val answers = answerDAO.getAll()

        return questions.map { question ->
            QuizItem(
                question,
                answers.filter { it.questionId == question.id })
        }
    }
}