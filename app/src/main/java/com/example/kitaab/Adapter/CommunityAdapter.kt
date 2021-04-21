package com.nikdev.kitaab.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nikdev.kitaab.R
import com.nikdev.kitaab.View.Channel

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
        //populate channel name in community recycler view
        holder.channelName.text = channels[position]

        //open channel conversation
        holder.channelName.setOnClickListener {
            val intent = Intent(context, Channel::class.java)
            intent.putExtra("name", channels[position])
            context.startActivity(intent)
        }
    }

}