package com.example.todoapp.activities

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.R
import com.example.todoapp.db.DatabaseHandler
import com.example.todoapp.model.Category
import com.example.todoapp.model.Task
import kotlinx.android.synthetic.main.activity_add_task.*
import java.util.*

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
        }

        addDate_Button.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, y, m, d ->
                dateChoosen_TextView.text = ""+ d + "/" + m + "/" + y
            }, year, month, day)

            dialog.show()
        }

        addTask_Button.setOnClickListener {
            if(checkIfAnyFieldIsEmpty()) {
                Toast.makeText(this, "Wypełnij wszystkie pola.", Toast.LENGTH_SHORT).show()
            } else {
                insertData()
            }
        }

        cancel_Button.setOnClickListener {
            finish()
        }
    }

    private fun checkIfAnyFieldIsEmpty() : Boolean {
        return taskName_EditText.text.toString().isEmpty()
    }

    private fun insertData() {
        val taskName = taskName_EditText.text.toString()
        val taskDate = dateChoosen_TextView.text.toString()

        println(taskCategory)
        val task = Task(taskName, taskDate, taskCategory)
        val db = DatabaseHandler(this)

        db.insertData(task)
    }
}
