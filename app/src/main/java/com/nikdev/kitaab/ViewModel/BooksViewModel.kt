package com.nikdev.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikdev.kitaab.Model.BookList
import com.nikdev.kitaab.Model.Genres
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class BooksViewModel: ViewModel() {

    private val databaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dataRef: DatabaseReference = databaseRef.reference
    val allGenres = MutableLiveData<MutableList<Genres>>()
    var userImage = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var userEmailAddress = MutableLiveData<String>()

    init {

        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                val userId = mAuth.currentUser.uid
                userImage.value = snapshot.child("users/$userId/image").value.toString()
                userName.value = snapshot.child("users/$userId/fullname").value.toString()
                userEmailAddress.value = snapshot.child("users/$userId/email").value.toString()

                val books = snapshot.child("books")
                val tempList : MutableList<Genres> = ArrayList()
                for(snap in books.children) {
                    val key = snap.key.toString()
                    val genreBooks: MutableList<BookList> = ArrayList()
                    for(snap2 in snap.children) {
                        val bookName = snap2.child("name").value.toString()
                        val bookImg = snap2.child("coverImg").value.toString()
                        val bookId = snap2.child("bookId").value.toString()
                        genreBooks.add(BookList(bookName,bookImg, bookId))
                    }
                    tempList.add(Genres(key,genreBooks))
                    allGenres.value = tempList
                }
            }
        }
        dataRef.addValueEventListener(data)

    }

}