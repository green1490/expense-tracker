package com.example.expensetracker.ui.expense

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.categories.CategoryAdapter

class Tags : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags)
    }

    override fun onStart() {
        super.onStart()
        val recyclerView = findViewById<RecyclerView>(R.id.categories)
        recyclerView.adapter = CategoryAdapter(this, mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}