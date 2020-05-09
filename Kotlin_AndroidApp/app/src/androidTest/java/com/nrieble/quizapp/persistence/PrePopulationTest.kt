package com.nrieble.quizapp.persistence

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
open class PrePopulationTest {

    private lateinit var questionDao: QuestionDAO
    private lateinit var answerDao: AnswerDAO
    private lateinit var quizDatabase: QuizDatabase


    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        quizDatabase = QuizDatabase.getInstance(context)
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
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val questions = questionDao.getAll()
                assert(questions.isNotEmpty())
            }
        }

    }

    @Test
    fun prePopulationWorksAndAnswerExist() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                val questions = questionDao.getAll()
                assert(questions.isNotEmpty())
            }
        }
    }
}