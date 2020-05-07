package com.nrieble.quizapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Answer(

    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: Int?,

    @ColumnInfo(name = "True")
    val truth: Int?,

    @ColumnInfo(name = "Text")
    val text: String?,

    // @ForeignKey()
    @ColumnInfo(name = "QuestionID")
    val questionId: Int?

)