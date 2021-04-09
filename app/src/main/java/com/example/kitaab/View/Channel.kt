package com.example.kitaab.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Adapter.MessageAdapter
import com.example.kitaab.Model.Message
import com.example.kitaab.R
import com.example.kitaab.ViewModel.ChannelViewModel

class Channel : AppCompatActivity() {

    private lateinit var typedMessage: EditText
    private lateinit var backButton: TextView
    private lateinit var noMessages: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var sendMsgButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        typedMessage = findViewById(R.id.typedMessage)
        backButton = findViewById(R.id.backButton)
        noMessages = findViewById(R.id.noMsg)
        recyclerView = findViewById(R.id.messageRecycler)
        sendMsgButton = findViewById(R.id.send)
        val name = intent.getStringExtra("name").toString()
        backButton.text = name

        val msgViewModel = ViewModelProvider(this).get(ChannelViewModel::class.java)
        msgViewModel.setCommName(name)
        msgViewModel.messages.observe(this, Observer {
            if(it.isNotEmpty()) {
                noMessages.isVisible = false
                setRecycler(it)
            }
        })

        backButton.setOnClickListener {
            this.finish()
        }
        sendMsgButton.setOnClickListener {
            if(typedMessage.text.isNotEmpty()) {
                noMessages.isVisible = false
                val msg = typedMessage.text.toString()
                msgViewModel.sendMessage(msg)
                typedMessage.text = null
            }
        }

    }

    private fun setRecycler(genres: List<Message>) {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        layoutManager.scrollToPosition(genres.size-1)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = MessageAdapter(this, genres)
    }
}