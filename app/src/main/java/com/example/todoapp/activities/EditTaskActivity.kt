package com.example.todoapp.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.TextView
import android.widget.Toast
import com.example.todoapp.R
import com.example.todoapp.db.DatabaseHandler
import com.example.todoapp.model.Category
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_edit_task.*

class EditTaskActivity : AppCompatActivity() {

    private var db = DatabaseHandler(this)
    private var id = 0
    private var taskCategory: Category = Category.Work

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        setupData()
        prepareButtons()
    }

    private fun setupData() {
        db = DatabaseHandler(this)
        id = intent.getIntExtra(TaskListActivity.TASK_KEY, 0)
        val task = db.readSingleRecord(id)

        taskCategory = task.category

        taskName_EditTaskEditText.setText(task.name)
        dateChoosen_EditTaskTextView.text = task.date

        when(task.category) {
            Category.Work -> taskCategory_EditTaskRadioGroup.check(categoryWork_EditTaskRadioButton.id)
            Category.Shopping -> taskCategory_EditTaskRadioGroup.check(categoryShopping_EditTaskRadioButton.id)
            else -> taskCategory_EditTaskRadioGroup.check(categoryOther_EditTaskRadioButton.id)
        }
    }

    private fun prepareButtons() {
        taskCategory_EditTaskRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            taskCategory = when(checkedId) {
                categoryWork_EditTaskRadioButton.id -> Category.Work
                categoryShopping_EditTaskRadioButton.id -> Category.Shopping
                else -> Category.Other
            }
        }

        editDate_EditTaskButton.setOnClickListener {
            val date = dateChoosen_EditTaskTextView.text.toString()

            val dateParts = date.split("/")
            var day = dateParts[0]
            var month = dateParts[1]
            val year = dateParts[2]

            if(day[0].equals('0')) day = day[1].toString()
            if(month[0].equals('0')) month = month[1].toString()

            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, y, m, d ->
                switchDateType(d, m, y, dateChoosen_EditTaskTextView)
            }, year.toInt(), month.toInt(), day.toInt())

            dialog.show()
        }

        editTask_EditTaskButton.setOnClickListener {
            if(checkIfAnyFieldIsEmpty()) {
                Toast.makeText(this, "Pole 'Nazwa' nie może być puste.", Toast.LENGTH_SHORT).show()
            } else {
                updateData()
                finish()
                val intent = Intent(this, TaskListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        deleteTask_EditTaskButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("UWAGA!")
                .setMessage("Czy chcesz usunąć zadanie?")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    db.deleteData(id)
                    finish()
                    val intent = Intent(this, TaskListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                })
                .setNegativeButton("Anuluj", DialogInterface.OnClickListener { dialog, which ->

                })
                .show()
        }

        cancel_EditTaskButton.setOnClickListener {
            finish()
        }
    }

    private fun switchDateType(d: Int,m: Int, y: Int, editText: TextView) {
        val mm = m+1

        var day: String = d.toString()
        var month: String = mm.toString()
        val year: String = y.toString()

        if(d < 10) day = "0$d"
        if(mm < 10) month = "0$mm"

        editText.text = "$day/$month/$year"
    }

    private fun checkIfAnyFieldIsEmpty() : Boolean {
        return taskName_EditTaskEditText.text.toString().isEmpty()
    }

    private fun updateData() {
        val taskName = taskName_EditTaskEditText.text.toString()
        val taskDate = dateChoosen_EditTaskTextView.text.toString()

        db.updateData(id, taskName, taskDate, taskCategory)
    }
}
