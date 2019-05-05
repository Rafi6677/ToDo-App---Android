package com.example.todoapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.todoapp.R
import com.example.todoapp.db.DatabaseHandler
import kotlinx.android.synthetic.main.activity_edit_task.*

class EditTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        setupData()
    }

    private fun setupData() {
        val db = DatabaseHandler(this)
        val id = intent.getIntExtra(TaskListActivity.TASK_KEY, 0)
        /*val task = db.readSingleRecord(id)

        taskName_EditTaskEditText.setText(task.name)*/
    }
}
