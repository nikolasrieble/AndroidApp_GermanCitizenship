package com.nrieble.quizapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nrieble.quizapp.domain.Question
import com.nrieble.quizapp.domain.Quiz

class QuizActivity : AppCompatActivity() {
    val EXTRA_SCORE = "extraScore"

    // layout
    private lateinit var textViewQuestion: TextView
    private lateinit var textViewScore: TextView
    private lateinit var textViewCount: TextView
    private lateinit var textViewCountdown: TextView
    private lateinit var buttonConfirmNext: Button
    private lateinit var imageViewQuestion: ImageView

    private lateinit var recyclerView: RecyclerView

    // database
    private lateinit var questionList: MutableList<Question>
    private lateinit var currentQuestion: Question

    private lateinit var quiz: Quiz

    // interaction
    private var questionCounter: Int = 0
    private var questionCountTotal: Int = 0
    var score: Float = 0.0F
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        imageViewQuestion = findViewById(R.id.imageView)
        textViewQuestion = findViewById(R.id.text_view_question)
        textViewScore = findViewById(R.id.text_view_score)

        textViewCount = findViewById(R.id.text_view_count)
        textViewCountdown = findViewById(R.id.text_view_countdown)
        buttonConfirmNext = findViewById(R.id.ConfirmAnswer)
        recyclerView = findViewById(R.id.AnswerRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbhelper = DBHelper(context = this)
        questionList = dbhelper.getAllQuestions()
        questionCountTotal = questionList.size

        quiz = Quiz(questionList)

        questionCounter = loadLastQuestion() - 1

        showNextQuestion()

        buttonConfirmNext.setOnClickListener {
            proceed()
        }
    }

    private fun proceed() {
        when (this.quiz.state) {
            Quiz.QuizState.READY ->
                if (this.currentQuestion.answer_selected()) {
                    this.quiz.answer(this.currentQuestion)
                    checkAnswer()
                } else {
                    Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                }

            Quiz.QuizState.REVIEW -> showNextQuestion()
        }
    }

    private fun showNextQuestion() {
        // get next question
        this.currentQuestion = this.quiz.getNextQuestion()

        updateQuestionActivityView(this.currentQuestion)

        // update shared prefs
        updateLastQuestion(questionCounter)

        buttonConfirmNext.text = "Next Question"
    }

    private fun updateQuestionActivityView(question: Question) {
        // setup question text
        textViewQuestion.text = question.question
        // setup answer options
        recyclerView.adapter = AnswerAdapter(question, this)
        // setup image

        if (question.image != "None") {
            val imageName = question.image.takeLast(7).take(3)
            val image = ContextCompat.getDrawable(
                this,
                this.resources.getIdentifier(
                    "q$imageName",
                    "drawable",
                    this.packageName
                )
            )
            imageViewQuestion.setImageDrawable(image)
        } else imageViewQuestion.setImageDrawable(null)

        // update counter
        questionCounter += 1
        textViewCount.text = "Question: $questionCounter/$questionCountTotal"
    }

    private fun checkAnswer() {
        // update view showing the correct answer
        this.currentQuestion.disclosure = true

        recyclerView.adapter = AnswerAdapter(this.currentQuestion, this)

        textViewScore.text = "Score: ${quiz.score}"
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
