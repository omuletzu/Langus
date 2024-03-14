package com.example.sql

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter_saved(val str_array : ArrayList<String>) : RecyclerView.Adapter<adapter_saved.viewClass>() {

    inner class viewClass(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val communityWord: TextView? = itemView.findViewById(R.id.community_word)

        fun Bind(str : String){
            communityWord?.text = str
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_saved, parent, false)
        return viewClass(view)
    }

    override fun onBindViewHolder(holder: viewClass, position: Int) {
        holder.Bind(str_array[position])
    }

    override fun getItemCount(): Int {
        return str_array.size
    }
}