package com.example.todoapp.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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

        supportActionBar?.title = "Nowe zadanie:"

        prepareButtons()
    }

    private fun prepareButtons() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        switchDateType(day, month, year, addDate_AddTaskButton)

        taskCategory_AddTaskRadioGroup.check(categoryWork_AddTaskRadioButton.id)

        taskCategory_AddTaskRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            taskCategory = when(checkedId) {
                categoryWork_AddTaskRadioButton.id -> Category.Work
                categoryShopping_AddTaskRadioButton.id -> Category.Shopping
                else -> Category.Other
            }
        }

        addDate_AddTaskButton.setOnClickListener {
            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, y, m, d ->
                switchDateType(d, m, y, addDate_AddTaskButton)
            }, year, month, day)

            dialog.show()
        }

        addTask_AddTaskButton.setOnClickListener {
            if(checkIfAnyFieldIsEmpty()) {
                Toast.makeText(this, "Pole 'Nazwa' nie może być puste.", Toast.LENGTH_SHORT).show()
            } else {
                insertData()
                finish()
                val intent = Intent(this, TaskListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        cancel_AddTaskButton.setOnClickListener {
            finish()
        }
    }

    private fun switchDateType(d: Int,m: Int, y: Int, button: Button) {
        val mm = m+1

        var day: String = d.toString()
        var month: String = mm.toString()
        val year: String = y.toString()

        if(d < 10) day = "0$d"
        if(mm < 10) month = "0$mm"

        button.text = "$day/$month/$year"
    }

    private fun checkIfAnyFieldIsEmpty() : Boolean {
        return taskName_AddTaskEditText.text.toString().isEmpty()
    }

    private fun insertData() {
        val taskName = taskName_AddTaskEditText.text.toString()
        val taskDate = addDate_AddTaskButton.text.toString()

        println(taskCategory)
        val task = Task(taskName, taskDate, taskCategory)
        val db = DatabaseHandler(this)

        db.insertData(task)
    }
}
