package com.example.kitaab.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Model.Genres
import com.example.kitaab.R
import com.example.kitaab.View.Channel
import com.google.firebase.storage.FirebaseStorage

class CommunityAdapter(private val context: Context, private val channels: List<String>) : RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var channelName: TextView = itemView.findViewById(R.id.channelName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        return CommunityViewHolder(LayoutInflater.from(context).inflate(R.layout.channel_recycler_item, parent, false))
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.channelName.text = channels[position]
        holder.channelName.setOnClickListener {
            val intent = Intent(context, Channel::class.java)
            intent.putExtra("name", channels[position])
            context.startActivity(intent)
        }
    }

}