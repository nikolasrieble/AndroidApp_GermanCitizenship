package com.nrieble.quizapp.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuestionDAO {
    @Query("SELECT * FROM Question")
    suspend fun getAll(): List<Question>
}