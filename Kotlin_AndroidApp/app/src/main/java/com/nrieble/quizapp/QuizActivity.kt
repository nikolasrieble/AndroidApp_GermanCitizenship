package com.nrieble.quizapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nrieble.quizapp.domain.Question
import com.nrieble.quizapp.domain.Quiz
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
    val EXTRA_SCORE = "extraScore"

    // database
    private lateinit var questionList: MutableList<Question>
    private lateinit var currentQuestion: Question

    private lateinit var quiz: Quiz

    // interaction
    private var questionCountTotal: Int = 0
    var score: Float = 0.0F
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        AnswerRecyclerView.layoutManager = LinearLayoutManager(this)

        val dbhelper = DBHelper(context = this)
        questionList = dbhelper.getAllQuestions()
        questionCountTotal = questionList.size

        quiz = Quiz(questionList)


        showNextQuestion()

        confirm_answer.setOnClickListener {
            proceed()
        }
    }

    private fun proceed() {
        when (this.currentQuestion.state) {
            Question.AnswerState.READY ->
                if (this.currentQuestion.answerSelected()) {
                    this.quiz.answer(this.currentQuestion)
                    checkAnswer()
                } else {
                    Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                }

            Question.AnswerState.REVIEW -> showNextQuestion()
        }
    }

    private fun showPreviousQuestion() {
        // get next question
        this.currentQuestion = this.quiz.getPreviousQuestion()
    }

    private fun showNextQuestion() {
        // get next question
        this.currentQuestion = this.quiz.getNextQuestion()

        updateQuestionActivityView(this.currentQuestion)

        // update shared prefs
        updateLastQuestion(quiz.progress.questionIndex)

        confirm_answer.text = "Next Question"
    }

    private fun updateQuestionActivityView(question: Question) {
        // setup question text
        text_view_question.text = question.question
        // setup answer options
        AnswerRecyclerView.adapter = AnswerAdapter(question, this)
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
            imageView.setImageDrawable(image)
        } else imageView.setImageDrawable(null)

        // update counter
        text_view_count.text = "Question: ${quiz.progress.questionIndex + 1}/$questionCountTotal"
    }

    private fun checkAnswer() {
        // update view showing the correct answer
        this.currentQuestion.disclosure = true

        AnswerRecyclerView.adapter = AnswerAdapter(this.currentQuestion, this)

        text_view_score.text = "Score: ${quiz.progress.score}"
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
