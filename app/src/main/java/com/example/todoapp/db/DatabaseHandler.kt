package com.example.todoapp.db

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AlertDialog
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

        if(result == -1.toLong()) {
            AlertDialog.Builder(context)
                .setTitle("Błąd!")
                .setMessage("Czy chcesz ponowić próbę?")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                   insertData(task)
                })
                .setNegativeButton("Anuluj", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
        } else {
            Toast.makeText(context, "Pomyślnie dodano zadanie.", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    fun readData() : MutableList<Task> {
        val list : MutableList<Task> = ArrayList()
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

                val task = Task()

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

    fun readSingleRecord(id: Int) : Task {
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME + " WHERE $COL_ID = '$id'"
        val result = db.rawQuery(query, null)
        val task = Task()
        if(result.moveToFirst()) {
            val categoryStr = result.getString(result.getColumnIndex(COL_CATEGORY)).toString()
            val category = when (categoryStr) {
                "Work" -> Category.Work
                "Shopping" -> Category.Shopping
                else -> Category.Other
            }

            task.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
            task.name = result.getString(result.getColumnIndex(COL_NAME))
            task.date = result.getString(result.getColumnIndex(COL_DATE))
            task.category = category
        }

        result.close()
        db.close()

        return task
    }

    fun updateData(id: Int, name: String, date: String, category: Category) {
        val db = this.readableDatabase
        val query = "UPDATE $TABLE_NAME SET $COL_NAME = '$name', $COL_DATE = '$date', " +
                "$COL_CATEGORY = '${category.toString()}' WHERE $COL_ID = '$id'"

        db.execSQL(query)
        db.close()

        Toast.makeText(context, "Pomyślnie zaktualizowano dane.", Toast.LENGTH_SHORT).show()
    }

    fun deleteData(id: Int) {
        val db = this.writableDatabase

        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
    }
}