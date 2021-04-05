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
    private lateinit var dataRef: DatabaseReference

    fun markPage(page: Int, genre: String, id: Long) {
        dataRef = databaseRef.getReference("users/$userId/user_library")
        val data = object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    if(snap.child("genre").value == genre && snap.child("bookId").value == id) {
                        val dataRef2 = databaseRef.getReference("users/$userId/user_library/${snap.key}")
                        dataRef2.child("bookmark").setValue(page.toString())
                    }
                }
            }
        }
        dataRef.addValueEventListener(data)
    }

}