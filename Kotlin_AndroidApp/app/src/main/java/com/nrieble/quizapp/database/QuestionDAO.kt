package com.nrieble.quizapp.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuestionDAO {
    @Query("SELECT * FROM Question")
    fun getAll(): List<Question>

    @Query("SELECT * FROM Question where Category = (:category)")
    fun getAllByCategory(category: String): List<Question>
}