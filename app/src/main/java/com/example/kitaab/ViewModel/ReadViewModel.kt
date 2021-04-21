package com.nikdev.kitaab.ViewModel

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
                    //to get key of the book from the database that the user wants to read
                    if(snap.child("genre").value == genre && snap.child("bookId").value == bookId) {
                        key = snap.key.toString()
                    }
                }
            }
        }
        dataRef.addValueEventListener(data)
    }

    //to get genre and book Id from View
    fun setVariables(gen: String, id: String) {
        genre = gen
        bookId = id
    }

    //to mark user's last visited page in the database
    fun markPage(page: Int) {
        val dataRef2 = databaseRef.getReference("users/$userId/user_library/$key")
        dataRef2.child("bookmark").setValue(page.toString())
    }

}