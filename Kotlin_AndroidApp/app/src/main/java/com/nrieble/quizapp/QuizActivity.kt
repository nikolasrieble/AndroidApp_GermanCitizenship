package com.nrieble.quizapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuizActivity : AppCompatActivity() {
    val EXTRA_SCORE = "extraScore"

    //layout
    private lateinit var textViewQuestion: TextView
    private lateinit var textViewScore: TextView
    private lateinit var textViewCount: TextView
    private lateinit var textViewCountdown: TextView
    private lateinit var buttonConfirmNext: Button

    private lateinit var recyclerView: RecyclerView

    //database
    private lateinit var questionList: MutableList<Question>
    private lateinit var currentQuestion: Question

    //interaction
    private var questionCounter: Int = 0
    private var questionCountTotal: Int = 0
    var score: Float = 0.0F
    var answered: Boolean = false
    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        textViewQuestion = findViewById(R.id.text_view_question)
        textViewScore = findViewById(R.id.text_view_score)

        textViewCount = findViewById(R.id.text_view_count)
        textViewCountdown = findViewById(R.id.text_view_countdown)
        buttonConfirmNext = findViewById(R.id.button_next_question)
        recyclerView = findViewById(R.id.my_recycler_view)

        /*val dbhelper = DatabaseHelper(context = this)
        questionList = dbhelper.getAllQuestions()*/

        val dbhelper = DBHelper(context = this)
        questionList = dbhelper.getAllQuestions()
        questionCountTotal = questionList.size


        showNextQuestion()

        buttonConfirmNext.setOnClickListener {
            //if the button has not been clicked before and at least one answer is clicked
            if (!answered) {
                if (true) {
                    checkAnswer()
                } else {
                    Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                }
            } else {
                showNextQuestion()
            }
        }
    }

    private fun showNextQuestion() {
        if (questionCounter < questionCountTotal) {

            //get next question
            this.currentQuestion = questionList[questionCounter]
            //setup layout
            textViewQuestion.text = currentQuestion.question

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = AnswerAdapter(this.currentQuestion, this)

            questionCounter += 1
            textViewCount.text = "Question: " + questionCounter + "/" + questionCountTotal

            answered = false
            buttonConfirmNext.text = "Next Question"
        } else {
            finishQuiz()
        }
    }

    private fun checkAnswer() {
        answered = true
        //update view showing the correct answer
        this.currentQuestion.disclosure = true
        recyclerView.adapter = AnswerAdapter(this.currentQuestion, this)
        //count score if correct
        score += this.currentQuestion.score()
        textViewScore.text = "Score: " + score.toString()
    }

    fun finishQuiz() {
        var resultIntent = Intent()
        resultIntent.putExtra(EXTRA_SCORE, score)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz()
        } else {
            Toast.makeText(this, "Press back again to quit", Toast.LENGTH_SHORT)
        }
        backPressedTime = System.currentTimeMillis()
    }

}
