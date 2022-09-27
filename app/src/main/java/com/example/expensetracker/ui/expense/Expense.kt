package com.example.expensetracker.ui.expense

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.expensetracker.R

class Expense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
    }

    override fun onStart() {
        super.onStart()
        val button = findViewById<Button>(R.id.expense_save)
        button.setOnClickListener {
            //TO DO: extract data and save it
            finish()
        }
    }
}