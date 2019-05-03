package com.example.todoapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.todoapp.model.Category
import com.example.todoapp.model.Task


const val DATABASE_NAME = "ToDoDB"
const val TABLE_NAME = "Tasks"
const val COL_ID = "id"
const val COL_NAME = "name"
const val COL_DATE = "date"
const val COL_CATEGORY = "category"

class DatabaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " VARCHAR(250), " +
                COL_DATE + " VARCHAR(20), " +
                COL_CATEGORY + " VARCHAR(8))"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    fun insertData(task: Task) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COL_NAME, task.name)
        contentValues.put(COL_DATE, task.date)
        contentValues.put(COL_CATEGORY, task.category.toString())

        val result = db.insert(TABLE_NAME, null, contentValues)

        if(result == -1.toLong())
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }

    fun readData() : MutableList<Task> {
        var list : MutableList<Task> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME
        val result = db.rawQuery(query, null)

        if(result.moveToFirst()) {
            do{
                val categoryStr = result.getString(result.getColumnIndex(COL_CATEGORY)).toString()
                val category = when(categoryStr) {
                    "Work" -> Category.Work
                    "Shopping" -> Category.Shopping
                    else -> Category.Other
                }

                println(category)

                var task = Task()
                task.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                task.name = result.getString(result.getColumnIndex(COL_NAME))
                task.date = result.getString(result.getColumnIndex(COL_DATE))
                task.category = category

                list.add(task)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }
}