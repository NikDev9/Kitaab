package com.nikdev.kitaab.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.nikdev.kitaab.Model.Message
import com.nikdev.kitaab.R
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(private val context: Context, private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var senderMessage: TextView = itemView.findViewById(R.id.senderMessage)
        var userMessage: TextView = itemView.findViewById(R.id.userMessage)
        var senderName: TextView = itemView.findViewById(R.id.senderName)
        var senderMessageLayout: LinearLayout = itemView.findViewById(R.id.messageLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.channel_recycler_message, parent, false))
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser!!.uid

        if(userId == messages[position].userId) {
            holder.userMessage.isVisible = true
            holder.userMessage.text = messages[position].message
        }
        else {
            holder.senderMessageLayout.isVisible = true
            holder.senderMessage.text = messages[position].message
            holder.senderName.text = messages[position].userName
        }

    }
}