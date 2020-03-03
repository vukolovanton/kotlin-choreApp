package com.example.choreapp.model

import java.text.DateFormat
import java.util.*

//Инстанс одной задачи
class Chore() {
    var choreName: String? = null
    var assignedBy: String? = null
    var assignedTo: String? = null
    var timeAssigned: Long? = null
    var id: Int? = null

    constructor(choreName: String, assignedBy: String, assignedTo: String, timeAssigned: Long, id: Int): this() {
        this.choreName = choreName
        this.assignedBy = assignedBy
        this.assignedTo = assignedTo
        this.timeAssigned = timeAssigned
        this.id = id
    }

    fun showHumanDate(timeAssigned: Long): String {
        val dateFormat: java.text.DateFormat = DateFormat.getDateTimeInstance()
        return dateFormat.format(Date(timeAssigned).time)
    }
}