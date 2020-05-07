package com.nrieble.quizapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
open class QuizDatabaseTest {

    private lateinit var questionDao: QuestionDAO
    private lateinit var answerDao: AnswerDAO
    private lateinit var quizDatabase: QuizDatabase


    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        quizDatabase = Room.databaseBuilder(context, QuizDatabase::class.java, "test.db")
            .createFromAsset("AllGermanCitizenshipV2.db")
            .build()
        questionDao = quizDatabase.questionDao()
        answerDao = quizDatabase.answerDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        quizDatabase.close()
    }

    @Test
    fun prePopulationWorksAndAQuestionsExist() {
        val questions = questionDao.getAll()
        assert(questions.isNotEmpty())
    }

    @Test
    fun prePopulationWorksAndAnswerExist() {
        val questions = questionDao.getAll()
        assert(questions.isNotEmpty())
    }
}