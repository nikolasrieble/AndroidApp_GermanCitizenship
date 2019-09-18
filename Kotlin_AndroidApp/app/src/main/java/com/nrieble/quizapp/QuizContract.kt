package com.nrieble.quizapp

import android.provider.BaseColumns

class QuizContract : BaseColumns {

    class QuestionTable : BaseColumns {
        val TABLE_NAME: String = "Question"
        val COLUMN_ID: String = "ID"
        val COLUMN_TEXT: String = "Text"
        val COLUMN_IMAGE: String = "Image"
    }

    class AnswerTable : BaseColumns {
        val TABLE_NAME: String = "Answer"
        val COLUMN_ID: String = "ID"
        val COLUMN_QUESTIONID: String = "QuestionID"
        val COLUMN_TEXT: String = "Text"
        val COLUMN_TRUTH: String = "Truth"
    }
}