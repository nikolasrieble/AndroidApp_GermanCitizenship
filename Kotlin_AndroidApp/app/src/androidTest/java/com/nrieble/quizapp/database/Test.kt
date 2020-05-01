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
    private lateinit var quizDatabase: QuizDatabase

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        quizDatabase = Room.databaseBuilder(context, QuizDatabase::class.java, "test.db")
            .createFromAsset("AllGermanCitizenship.db")
            .build()
        questionDao = quizDatabase.questionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        quizDatabase.close()
    }

    @Test
    fun prePopulationWorks() {
        val questions = questionDao.getAll()
        assert(questions.isNotEmpty())
    }
}
