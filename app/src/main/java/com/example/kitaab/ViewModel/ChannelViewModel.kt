package com.example.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitaab.Model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChannelViewModel : ViewModel() {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mAuth = FirebaseAuth.getInstance()
    private val userId = mAuth.currentUser!!.uid

    private var key = ""
    private var username = ""
    var messages = MutableLiveData<List<Message>>()

    //to get genre name from view and retrieve messages of that genre's channel
    fun setCommName(name: String) {
        val data = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                username = snapshot.child("users/$userId/fullname").value.toString()

                //list of Message data objects
                val tempList: MutableList<Message> = ArrayList()
                val community = snapshot.child("community")
                for(snap in community.children) {
                    if(snap.child("name").value == name) {
                        key = snap.key.toString()
                        val snap2 = snap.child("messages")
                        for(messages in snap2.children) {
                            val msg = messages.child("message").value.toString()
                            val userId = messages.child("userId").value.toString()
                            val userName = messages.child("userName").value.toString()
                            tempList.add(Message(msg, userId, userName))
                        }
                    }
                }
                messages.value = tempList
            }
        }
        databaseRef.addValueEventListener(data)
    }

    //add message to channel in the database
    fun sendMessage(msg: String) {
        val dataRef = databaseRef.child("community/$key/messages")
        val childRef = dataRef.push()
        val key = childRef.key.toString()
        dataRef.child(key).setValue(Message(msg, userId, username))
    }

}