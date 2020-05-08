package com.nrieble.quizapp.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AnswerDAO {
    @Query("SELECT * FROM Answer")
    fun getAll(): List<Answer>

    @Query("SELECT * FROM Answer where QuestionID = (:questionId)")
    fun getAllForQuestion(questionId: String): List<Answer>
}