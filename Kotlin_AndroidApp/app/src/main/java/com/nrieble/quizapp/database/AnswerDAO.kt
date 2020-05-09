package com.nrieble.quizapp.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AnswerDAO {
    @Query("SELECT * FROM Answer")
    suspend fun getAll(): List<Answer>

    @Query("SELECT * FROM Answer where QuestionID = (:questionId)")
    suspend fun getAllForQuestion(questionId: String): List<Answer>
}