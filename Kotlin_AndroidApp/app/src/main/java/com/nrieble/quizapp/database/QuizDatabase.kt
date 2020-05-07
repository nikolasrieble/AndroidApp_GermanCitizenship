package com.nrieble.quizapp.database


import androidx.room.Database
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
}