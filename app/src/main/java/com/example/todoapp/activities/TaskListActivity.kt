package com.example.todoapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.todoapp.R
import com.example.todoapp.db.DatabaseHandler
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()

    companion object {
        const val TASK_KEY = "taskKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        prepareButton()
        setupData()
    }

    private fun prepareButton() {
        addNewTask_Button.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupData() {
        val db = DatabaseHandler(this)
        val data = db.readData()

        checkIfTaskListIsEmpty()

        for(i in 0 until data.size) {
            val taskId = data[i].id
            val taskName = data[i].name
            val taskDate = data[i].date
            val taskCategory = data[i].category

            val task = Task(taskId, taskName, taskDate, taskCategory)

            adapter.add(TaskItem(task))
        }

        adapter.setOnItemClickListener { item, view ->
            val taskItem = item as TaskItem
            val id = taskItem.task.id
            val intent = Intent(this, EditTaskActivity::class.java)
            intent.putExtra(TASK_KEY, id)
            startActivity(intent)
        }

        adapter.setOnItemLongClickListener { item, view ->
            val taskItem = item as TaskItem
            val id = taskItem.task.id

            AlertDialog.Builder(this)
                .setTitle("UWAGA!")
                .setMessage("Czy chcesz usunąć zadanie?")
                .setPositiveButton("OK") { _, _ ->
                    db.deleteData(id)
                    adapter.clear()
                    setupData()
                }
                .setNegativeButton("Anuluj") { _, _ -> }
                .show()

            item.isLongClickable
        }

        taskList_RecyclerView.adapter = adapter
    }

    private fun checkIfTaskListIsEmpty() {
        val db = DatabaseHandler(this)
        val data = db.readData()

        if(data.size == 0) {
            taskName_TextViewRowTitle.visibility = View.INVISIBLE
            taskCategory_TextViewRowTitle.visibility = View.INVISIBLE
            taskDate_TextViewRowTitle.visibility = View.INVISIBLE

            taskListTitle_TextView.text = getString(R.string.empty_task_list_title)
        } else {
            taskName_TextViewRowTitle.visibility = View.VISIBLE
            taskCategory_TextViewRowTitle.visibility = View.VISIBLE
            taskDate_TextViewRowTitle.visibility = View.VISIBLE

            taskListTitle_TextView.text = getString(R.string.task_list_title)
        }
    }
}


