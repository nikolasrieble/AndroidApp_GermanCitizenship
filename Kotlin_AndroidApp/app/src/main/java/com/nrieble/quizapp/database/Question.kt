package com.nrieble.quizapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(

    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: Int?,

    @ColumnInfo(name = "Category")
    val category: String?,

    @ColumnInfo(name = "Text")
    val text: String?,

    @ColumnInfo(name = "Image")
    val image: String?

)
