package com.example.expensetracker

data class ExpenseData(
    var sum:        String,
    var category:   String,
    var description:String?,
    var icon:       Int,
    var isChecked:  Boolean
)
