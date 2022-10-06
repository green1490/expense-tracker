package com.example.expensetracker.ui.expense

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        recyclerView.adapter = CategoryAdapter(
            this, mutableListOf(
                "education",
                "food","house",
                "injury",
                "shop",
                "sport",
                "transport",
                "video game",
            ),
            mapOf(
                "education" to R.drawable.ic_education,
                "food" to R.drawable.ic_food,
                "house" to R.drawable.ic_house,
                "injury" to R.drawable.ic_injury,
                "shop" to R.drawable.ic_shop,
                "sport" to R.drawable.ic_sport,
                "transport" to R.drawable.ic_transport,
                "video game" to R.drawable.ic_video_game,
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}