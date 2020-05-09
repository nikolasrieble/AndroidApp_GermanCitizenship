package com.nrieble.quizapp.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuestionDAO {
    @Query("SELECT * FROM Question")
    suspend fun getAll(): List<Question>

    @Query("SELECT * FROM Question where Category = (:category)")
    suspend fun getAllByCategory(category: String): List<Question>
}