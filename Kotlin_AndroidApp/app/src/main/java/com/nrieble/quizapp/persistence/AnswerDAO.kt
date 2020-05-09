package com.nrieble.quizapp.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AnswerDAO {
    @Query("SELECT * FROM Answer")
    suspend fun getAll(): List<Answer>
}