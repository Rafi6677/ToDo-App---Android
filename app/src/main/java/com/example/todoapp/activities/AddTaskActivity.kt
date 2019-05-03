package com.example.todoapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.R
import com.example.todoapp.db.DatabaseHandler
import com.example.todoapp.model.Category
import com.example.todoapp.model.Task
import kotlinx.android.synthetic.main.activity_add_task.*

class AddTaskActivity : AppCompatActivity() {

    private var taskCategory: Category = Category.Work

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        prepareButtons()
    }

    private fun prepareButtons() {
        taskCategory_RadioGroup.check(categoryWork_RadioButton.id)

        taskCategory_RadioGroup.setOnCheckedChangeListener { group, checkedId ->
            taskCategory = when(checkedId) {
                categoryWork_RadioButton.id -> Category.Work
                categoryShopping_RadioButton.id -> Category.Shopping
                else -> Category.Other
            }

            println(taskCategory)
        }

        cancel_Button.setOnClickListener {
            finish()
        }

        addTask_Button.setOnClickListener {
            if (checkIfAnyFieldIsEmpty()) {
                Toast.makeText(this, "Wypełnij wszystkie pola.", Toast.LENGTH_SHORT).show()
            } else {
                if (!checkIfDateFormatIsCorrect()) {
                    insertData()
                } else {
                    Toast.makeText(this, "Nieprawidłowy format daty.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkIfAnyFieldIsEmpty(): Boolean {
        return taskName_EditText.text.toString().isEmpty() ||
            taskDate_EditText.text.toString().isEmpty()
    }

    private fun checkIfDateFormatIsCorrect(): Boolean {
        val date = taskDate_EditText.text.toString()
        var correct = true

        date.forEachIndexed { index, char ->
            when(index) {
                2, 5 -> if(char == '/') correct = false
            }
        }

        return correct
    }

    private fun insertData() {
        val taskName = taskName_EditText.text.toString()
        val taskDate = taskDate_EditText.text.toString()

        val task = Task(taskName, taskDate, taskCategory)
        val db = DatabaseHandler(this)

        db.insertData(task)
    }
}
