package com.example.expensetracker.ui.expense

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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

        val addTagButton = findViewById<Button>(R.id.add_tag)
        addTagButton.setOnClickListener {
                val intent = Intent(this, Tags::class.java)
                startActivity(intent)
            }
    }
}