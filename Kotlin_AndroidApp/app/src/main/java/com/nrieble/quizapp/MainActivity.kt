package com.nrieble.quizapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.starting_screen.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_QUIZ = 1

    private val SHARED_PREFS = "sharedPrefs"
    private val KEY_HIGHSCORE = "keyHighscore"

    private var highscore: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.starting_screen)
        loadHighscore()
        button_start_quiz.setOnClickListener { startQuiz() }
    }

    private fun startQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_QUIZ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_QUIZ && resultCode == Activity.RESULT_OK) {
            val score = data?.getFloatExtra(QuizActivity().EXTRA_SCORE, 0.0F)
            if (score!! >= highscore) {
                updateHighscore(score)
            }
        }
    }

    private fun loadHighscore() {
        highscore =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getFloat(KEY_HIGHSCORE, 0.0F)
        text_view_highscore.text = "Highscore: $highscore"
    }

    private fun updateHighscore(score: Float) {
        this.highscore = score
        text_view_highscore.text = "Highscore: $highscore"

        val editor = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit()
        editor.putFloat(KEY_HIGHSCORE, highscore).apply()
    }
}
