package com.example.sql

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CommunityAdapter(val usernames: List<String>, private val words: List<String>, private val descriptions: List<String>) : RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {
    inner class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val communityUsername: TextView? = itemView.findViewById(R.id.community_username)
        private val communityWord: TextView? = itemView.findViewById(R.id.community_word)
        private val communityDesc: TextView? = itemView.findViewById(R.id.community_desc)

        fun bind(username: String, word: String, desc: String) {
            communityUsername?.text = username
            communityWord?.text = word
            communityDesc?.text = desc
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.bind(usernames[position], words[position], descriptions[position])
    }

    override fun getItemCount(): Int {
        return usernames.size
    }
}
