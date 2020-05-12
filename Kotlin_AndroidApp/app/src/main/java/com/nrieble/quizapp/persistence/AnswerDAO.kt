package com.nrieble.quizapp.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AnswerDAO {
    @Query("SELECT * FROM Answer")
    suspend fun getAll(): List<Answer>
    @Query("SELECT * FROM Answer where questionId = :questionId")
    suspend fun getBy(questionId: Int): List<Answer>
}