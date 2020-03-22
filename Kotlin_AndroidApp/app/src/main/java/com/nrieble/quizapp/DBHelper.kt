package com.nrieble.quizapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.nrieble.quizapp.domain.Question
import java.io.File
import java.io.FileOutputStream

// credits belong to https://stackoverflow.com/users/1749684/dardan - https://stackoverflow.com/questions/22627215/how-to-put-database-and-read-database-from-assets-folder-android-which-are-creat#22627776

class DBHelper(private val context: Context) : SQLiteOpenHelper(context, dbName, null, 1) {

    private var dataBase: SQLiteDatabase? = null

    init {
        val dbExist = checkDatabase()
        if (dbExist) {
            Log.e("-----", "Database exist")
        } else {
            Log.e("-----", "Database doesn't exist")
            createDatabase()
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private fun createDatabase() {
        copyDatabase()
    }

    private fun checkDatabase(): Boolean {
        val dbFile = File(context.getDatabasePath(dbName).path)
        return dbFile.exists()
    }

    private fun copyDatabase() {

        val inputStream = context.assets.open(dbName)

        val outputFile = File(context.getDatabasePath(dbName).path)
        val outputStream = FileOutputStream(outputFile)

        val bytesCopied = inputStream.copyTo(outputStream)
        Log.e("bytesCopied", "$bytesCopied")
        inputStream.close()

        outputStream.flush()
        outputStream.close()
    }

    fun getAllQuestions(): MutableList<Question> {

        val question_list = mutableListOf<Question>()
        val database = this.readableDatabase

        val SELECT_ALL_QUESTIONS = "select " +
                "Question.ID, " +
                "Question.Text, " +
                "Question.Image, " +
                "Question.Category, " +
                "group_concat(Answer.Text, '#') as Options, " +
                "group_concat(Answer.True, '#') as Truth " +
                "from Question " +
                "inner join Answer on Answer.QuestionID = Question.ID " +
                "group by Question.ID, Question.Text, Question.Image "

        val c: Cursor = database.rawQuery(SELECT_ALL_QUESTIONS, null)
        if (c.moveToFirst()) {
            do {
                val question = c.getString(c.getColumnIndex("Text"))
                val options = c.getString(c.getColumnIndex("Options")).split("#")
                val truth = c.getString(c.getColumnIndex("Truth")).split("#").map { it == "1" }
                val id = c.getString(c.getColumnIndex("ID")).toInt()
                val category = c.getString(c.getColumnIndex("Category"))
                val image = c.getString(c.getColumnIndex("Image"))
                val q = Question(
                    question,
                    options,
                    truth,
                    id,
                    image,
                    category
                )
                question_list.add(q)
            } while (c.moveToNext())
        }
        c.close()
        return question_list
    }

    override fun close() {
        dataBase?.close()
        super.close()
    }

    companion object {
        private const val dbName = "AllGermanCitizenship.db"
    }
}
