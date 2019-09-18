package com.nrieble.quizapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class DatabaseHelper : SQLiteOpenHelper {
    val DATABASE_NAME: String = "GermanCitizenship"
    val DATABASE_VERSION: Int = 1
    lateinit var database: SQLiteDatabase

    constructor(
        context: Context?
    ) : super(context, "GermanCitizenship", null, 1)
    //TODO: parametrize the above call

    override fun onCreate(db: SQLiteDatabase) {
        database = db

        val questions = QuizContract.QuestionTable()
        val answers = QuizContract.AnswerTable()

        val SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                questions.TABLE_NAME + " ( " +
                questions.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                questions.COLUMN_TEXT + " TEXT, " +
                questions.COLUMN_IMAGE + " TEXT)"

        val SQL_CREATE_ANSWERS_TABLE = "CREATE TABLE " +
                answers.TABLE_NAME + " ( " +
                answers.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                answers.COLUMN_QUESTIONID + " INT, " +
                answers.COLUMN_TRUTH + " BOOL, " +
                answers.COLUMN_TEXT + " TEXT)"

        db.execSQL(SQL_CREATE_ANSWERS_TABLE)
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE)

        fillQuestions()
    }

    fun loadExisting(context: Context?){
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionTable().TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.AnswerTable().TABLE_NAME)
        onCreate(db)
    }

    private fun fillQuestions() {
        val q1 = Question(
            "That is correct",
            listOf("this", "that", "those", "which"),
            listOf(false, true, false, false),
            1,
            ""
        )
        addQuestion(q1)
        val q2 = Question(
            "That and this are correct",
            listOf("this", "that", "those", "which"),
            listOf(true, true, false, false),
            2,
            ""
        )
        addQuestion(q2)
        val q3 = Question(
            "That and this are correct",
            listOf("this", "that", "those", "which"),
            listOf(true, true, false, false),
            3,
            ""
        )
        addQuestion(q3)
    }

    private fun addQuestion(q: Question) {

        var cvQ = ContentValues()
        cvQ.put(QuizContract.QuestionTable().COLUMN_ID, q.id)
        cvQ.put(QuizContract.QuestionTable().COLUMN_TEXT, q.question)
        cvQ.put(QuizContract.QuestionTable().COLUMN_IMAGE, q.image)
        database.insert(QuizContract.QuestionTable().TABLE_NAME, null, cvQ)

        for (i in 0..q.options.size - 1) {

            var cvA = ContentValues()
            cvA.put(QuizContract.AnswerTable().COLUMN_QUESTIONID, q.id)
            cvA.put(QuizContract.AnswerTable().COLUMN_TEXT, q.options[i])
            cvA.put(QuizContract.AnswerTable().COLUMN_TRUTH, q.truth[i])
            database.insert(QuizContract.AnswerTable().TABLE_NAME, null, cvA)
        }
    }

    fun getAllQuestions(): MutableList<Question> {

        var question_list = mutableListOf<Question>()
        val database = this.readableDatabase

        val SELECT_ALL_QUESTIONS = "select " +
                "Question.ID, " +
                "Question.Text, " +
                "Question.Image, " +
                "group_concat(Answer.Text, ',') as Options, " +
                "group_concat(Answer.Truth, ',') as Truth " +
                "from Question " +
                "inner join Answer on Answer.QuestionID = Question.ID " +
                "group by Question.ID, Question.Text, Question.Image "

        val c: Cursor = database.rawQuery(SELECT_ALL_QUESTIONS, null)
        if (c.moveToFirst()) {
            do {
                val question = c.getString(c.getColumnIndex("Text"))
                val options = c.getString(c.getColumnIndex("Options")).split(",")
                val truth = c.getString(c.getColumnIndex("Truth")).split(",").map { it.equals("1") }
                val id = c.getString(c.getColumnIndex("ID")).toInt()
                val image = c.getString(c.getColumnIndex("Image"))
                var q = Question(
                    question,
                    options,
                    truth,
                    id,
                    image
                )
                question_list.add(q)
            } while (c.moveToNext())
        }
        c.close()
        return question_list

    }
}