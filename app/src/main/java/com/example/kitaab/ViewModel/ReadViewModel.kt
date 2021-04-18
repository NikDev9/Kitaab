package com.example.kitaab.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ReadViewModel : ViewModel() {

    private val databaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId = mAuth.currentUser!!.uid
    private var dataRef: DatabaseReference
    private var key: String = ""
    lateinit var genre: String
    lateinit var bookId: String

    init {
        dataRef = databaseRef.getReference("users/$userId/user_library")
        val data = object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    if(snap.child("genre").value == genre && snap.child("bookId").value == bookId) {
                        key = snap.key.toString()
                    }
                }
            }
        }
        dataRef.addValueEventListener(data)
    }

    fun setVariables(gen: String, id: String) {
        genre = gen
        bookId = id
    }

    fun markPage(page: Int) {
        val dataRef2 = databaseRef.getReference("users/$userId/user_library/$key")
        dataRef2.child("bookmark").setValue(page.toString())
    }

}