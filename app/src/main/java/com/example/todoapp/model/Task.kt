package com.example.todoapp.model

import java.util.*

class Task {

    var id: Int = 0
    var name: String = ""
    var date: String = Date().toString()
    var category: Category = Category.Other

    constructor(name: String, date: String, category: Category) {
        this.name = name
        this.date = date
        this.category = category
    }

}