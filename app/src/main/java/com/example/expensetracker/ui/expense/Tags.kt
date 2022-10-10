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
                getString(R.string.education),
                getString(R.string.food),
                getString(R.string.house),
                getString(R.string.injury),
                getString(R.string.shop),
                getString(R.string.sport),
                getString(R.string.transport),
                getString(R.string.video_game),
            ),
            mapOf(
                getString(R.string.education) to R.drawable.ic_education,
                getString(R.string.food) to R.drawable.ic_food,
                getString(R.string.house) to R.drawable.ic_house,
                getString(R.string.injury) to R.drawable.ic_injury,
                getString(R.string.shop) to R.drawable.ic_shop,
                getString(R.string.sport) to R.drawable.ic_sport,
                getString(R.string.transport) to R.drawable.ic_transport,
                getString(R.string.video_game) to R.drawable.ic_video_game,
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}