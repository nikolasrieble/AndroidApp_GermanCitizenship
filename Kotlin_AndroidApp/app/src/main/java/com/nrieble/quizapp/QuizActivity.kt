package com.nrieble.quizapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nrieble.quizapp.database.QuizDatabase
import com.nrieble.quizapp.domain.QuizItem
import com.nrieble.quizapp.domain.Quiz
import com.nrieble.quizapp.domain.QuizItemRepository
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {
    val EXTRA_SCORE = "extraScore"

    // database
    private val quizDatabase = QuizDatabase.getInstance(this)
    private lateinit var currentQuizItem: QuizItem
    private lateinit var quiz: Quiz

    // interaction
    private var score: Float = 0.0F
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setup layout
        setContentView(R.layout.activity_quiz)
        AnswerRecyclerView.layoutManager = LinearLayoutManager(this)
        confirm_answer.setOnClickListener { proceed() }
        // get content
        CoroutineScope(Dispatchers.Main).launch {
            quiz = loadQuiz()
            showNextQuestion()
        }
    }

    private suspend fun loadQuiz(): Quiz {
        val repository = QuizItemRepository(
            quizDatabase.questionDao(),
            quizDatabase.answerDao()
        )
        return Quiz(repository.getQuizItems())
    }

    private fun proceed() {
        when (this.currentQuizItem.state) {
            QuizItem.AnswerState.READY ->
                if (this.currentQuizItem.answerSelected()) {
                    this.quiz.answer(this.currentQuizItem)
                    checkAnswer()
                } else {
                    Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                }

            QuizItem.AnswerState.REVIEW -> showNextQuestion()
        }
    }

    private fun showPreviousQuestion() {
        // get next question
        this.currentQuizItem = this.quiz.getPreviousQuestion()
    }

    private fun showNextQuestion() {
        // get next question
        this.currentQuizItem = this.quiz.getNextQuestion()

        updateQuestionActivityView(this.currentQuizItem)

        // update shared prefs
        updateLastQuestion(quiz.state.questionIndex)

        confirm_answer.text = "Next Question"
    }

    private fun updateQuestionActivityView(quizItem: QuizItem) {
        // setup question text
        text_view_question.text = quizItem.question.text.toString()
        // setup answer options
        AnswerRecyclerView.adapter = AnswerAdapter(quizItem, this)
        // setup image

        if (quizItem.question.image != "None") {
            val imageName = quizItem.question.image?.takeLast(7)?.take(3)
            val image = ContextCompat.getDrawable(
                this,
                this.resources.getIdentifier(
                    "q$imageName",
                    "drawable",
                    this.packageName
                )
            )
            imageView.setImageDrawable(image)
        } else imageView.setImageDrawable(null)

        // update counter
        text_view_count.text = "Question: ${quiz.state.questionIndex + 1}/${quiz.size}"
    }

    private fun checkAnswer() {
        // update view showing the correct answer
        this.currentQuizItem.disclosure = true

        AnswerRecyclerView.adapter = AnswerAdapter(this.currentQuizItem, this)

        text_view_score.text = "Score: ${quiz.state.score}"
    }

    private fun finishQuiz() {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_SCORE, score)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz()
        } else {
            Toast.makeText(this, "Press back again to quit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun updateLastQuestion(questionNumber: Int) {
        val editor =
            applicationContext.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE).edit()
        editor.putInt("LastQuestionNumber", questionNumber).apply()
    }

    private fun loadLastQuestion(): Int {
        return applicationContext.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            .getInt("LastQuestionNumber", 1)
    }
}
