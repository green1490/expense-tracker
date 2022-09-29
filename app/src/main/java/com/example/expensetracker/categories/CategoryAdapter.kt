package com.example.expensetracker.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R

class CategoryAdapter(private var ct:Context,private var tags:List<String>):RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    class CustomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val textView: TextView

        init {
            textView = itemView.findViewById(R.id.category_text)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(ct)
            .inflate(R.layout.categories_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.textView.text = tags[position]
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}