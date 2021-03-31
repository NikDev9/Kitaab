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
                    val image = snap.child("filepath").value.toString()
                    val genre = snap.child("genre").value.toString()
                    val key = snap.key.toString()
                    tempList.add(FavBook(book, bookId, bookmark, image, genre))
                }
                favList.value = tempList
            }
        }
        dataRef.addValueEventListener(data)

    }

    fun removeFromFav(key: Int) {
        val dataRef2 = databaseRef.getReference("users/$userId")
        dataRef2.child("user_library/$key").removeValue()
    }

}