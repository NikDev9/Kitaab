package com.example.kitaab.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitaab.Model.FavBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavListViewModel : ViewModel() {

    private val databaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dataRef: DatabaseReference = databaseRef.reference
    var favList = MutableLiveData<List<FavBook>>()
    var numBooks: Long = 0

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId = mAuth.currentUser!!.uid

    init {

        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                //to get the number of books in user's fav list
                numBooks = snapshot.child("users/$userId/bookNum").value as Long
                val tempList: MutableList<FavBook> = ArrayList()

                val favBooks = snapshot.child("users/$userId/user_library")
                for(snap in favBooks.children) {
                    val book = snap.child("book").value.toString()
                    val bookId = snap.child("bookId").value as Long
                    val bookmark = snap.child("bookmark").value.toString()
                    val image = snap.child("coverImg").value.toString()
                    val genre = snap.child("genre").value.toString()
                    val filename = snap.child("filename").value.toString()
                    tempList.add(FavBook(book, bookId, bookmark, filename, image, genre))
                }
                favList.value = tempList
            }
        }
        dataRef.addValueEventListener(data)

    }

    fun removeFromFav(genre: String, bookId: Long) {
        val dataRef2 = databaseRef.getReference("users/$userId/user_library")
        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    if(snap.child("genre").value == genre && snap.child("bookId").value == bookId) {
                        val ref = databaseRef.getReference("users/$userId/user_library")
                        ref.child("${snap.key}").removeValue()
                    }
                }
            }
        }
        dataRef2.addValueEventListener(data)
    }

}