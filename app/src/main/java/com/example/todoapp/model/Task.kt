package com.example.todoapp.model

import java.util.*

class Task {

    var id: Int = 0
    var name: String = ""
    var date: Date = Date()
    var category: Category = Category.Other

    constructor(name: String, date: Date, category: Category) {
        this.name = name
        this.date = date
        this.category = category
    }

}