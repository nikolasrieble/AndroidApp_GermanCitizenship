package com.nrieble.quizapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_QUIZ = 1

    val SHARED_PREFS = "sharedPrefs"
    val KEY_HIGHSCORE = "keyHighscore"

    private lateinit var textViewHighscore: TextView
    private var highscore: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.starting_screen)

        MobileAds.initialize(this)

        textViewHighscore = findViewById(R.id.text_view_highscore)
        loadHighscore()

        val buttonStartQuiz = findViewById<Button>(R.id.button_start_quiz)
        buttonStartQuiz.setOnClickListener { startQuiz() }
    }

    fun startQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_QUIZ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == Activity.RESULT_OK) {
                val score = data?.getFloatExtra(QuizActivity().EXTRA_SCORE, 0.0F)
                if (score!! >= highscore) {
                    updateHighscore(score)
                }
            }
        }
    }

    private fun loadHighscore() {
        highscore =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getFloat(KEY_HIGHSCORE, 0.0F)
        textViewHighscore.text = "Highscore: $highscore"
    }

    private fun updateHighscore(score: Float) {
        this.highscore = score
        textViewHighscore.text = "Highscore: $highscore"

        val editor = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit()
        editor.putFloat(KEY_HIGHSCORE, highscore).apply()
    }

}
