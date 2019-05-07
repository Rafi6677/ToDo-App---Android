package com.example.todoapp.model

class Task {

    var id: Int = 1
    var name: String = ""
    var date: String = ""
    var category: Category = Category.Other

    constructor(name: String, date: String, category: Category) {
        this.name = name
        this.date = date
        this.category = category
    }

    constructor(id: Int, name: String, date: String, category: Category) {
        this.id = id
        this.name = name
        this.date = date
        this.category = category
    }

    constructor()
}