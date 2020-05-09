package com.nrieble.quizapp.persistence


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(
        Question::class,
        Answer::class
    ), version = 1
)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDAO
    abstract fun answerDao(): AnswerDAO

    companion object {

        private var INSTANCE: QuizDatabase? = null

        fun getInstance(context: Context): QuizDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, QuizDatabase::class.java, "quiz.db")
                    .createFromAsset("AllGermanCitizenshipV2.db")
                    .build()
            }
            return INSTANCE!!
        }
    }

}