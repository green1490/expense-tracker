package com.example.expensetracker.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.ExpenseData
import com.example.expensetracker.R

class CustomAdapter(private var ct:Context,private var tags:MutableList<ExpenseData>):RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {
//    val tags:MutableList<ExpenseData> = mutableListOf()
    var copytag = tags

    class CustomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        val imageView:ImageView

        init {
            textView = itemView.findViewById(R.id.textView)
            imageView = itemView.findViewById(R.id.imageViewTagIcon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(LayoutInflater.from(ct)
            .inflate(R.layout.list_row,parent,false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val sb = StringBuilder()
        sb.append(copytag[position].sum.plus(" "))
            .append(copytag[position].category.plus(" "))
            .append(copytag[position].description ?: "")
        holder.textView.text = sb.toString()
        holder.imageView.setImageResource(copytag[position].icon)
    }

    override fun getItemCount(): Int {
        return copytag.size
    }

}