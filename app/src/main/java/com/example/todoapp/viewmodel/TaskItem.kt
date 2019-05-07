package com.example.todoapp.viewmodel

import com.example.todoapp.R
import com.example.todoapp.model.Task
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.task_list_row.view.*

class TaskItem(val task: Task): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.task_list_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.taskName_TextViewRow.text = task.name
        viewHolder.itemView.taskDate_TextViewRow.text = task.date
        viewHolder.itemView.taskCategory_TextViewRow.text = when(task.category.toString()) {
            "Work" -> "Praca"
            "Shopping" -> "Zakupy"
            else -> "Inne"
        }
    }
}