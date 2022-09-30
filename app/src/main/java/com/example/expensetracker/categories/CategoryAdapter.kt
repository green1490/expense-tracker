package com.example.expensetracker.categories

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R

class CategoryAdapter(private var ct:Context,private var tags:MutableList<String>):RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    class CustomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val textView:       TextView
        val categoryCard:   CardView

        init {
            textView = itemView.findViewById(R.id.category_text)
            categoryCard = itemView.findViewById(R.id.card_category)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(ct)
            .inflate(R.layout.categories_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.textView.text = tags[position]


        holder.categoryCard.setOnClickListener {
                (ct as Activity).setResult(Activity.RESULT_OK,Intent().putExtra("tag",tags[position]))
                (ct as Activity).finish()
        }


    }

    override fun getItemCount(): Int {
        return tags.size
    }
}