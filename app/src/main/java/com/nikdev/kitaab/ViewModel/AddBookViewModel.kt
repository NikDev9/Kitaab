package com.nikdev.kitaab.ViewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikdev.kitaab.Model.Book
import com.nikdev.kitaab.Model.FavBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddBookViewModel : ViewModel() {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storage: StorageReference = FirebaseStorage.getInstance().reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId = mAuth.currentUser!!.uid
    var author: String = ""
    var genres = MutableLiveData<List<String>>()

    init {
        val data = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val userSnapShot = snapshot.child("users/$userId")
                author = userSnapShot.child("fullname").value.toString()

                val tempList = ArrayList<String>()
                val genreSnapShot = snapshot.child("books/")
                for(snap in genreSnapShot.children)
                    tempList.add(snap.key.toString())
                genres.value = tempList
            }
        }
        databaseRef.addValueEventListener(data)
    }

    fun uploadFiles(pdf: Uri, img: Uri, name: String, synopsis: String, genre: String) {

        //add book to main library in database
        val dataRef2 = databaseRef.child("books/$genre")
        val childRef2 = dataRef2.push()
        val key2 = childRef2.key.toString()
        dataRef2.child(key2).setValue(Book(author, key2, "book covers/$key2.jpg", "books/$key2.pdf", name, synopsis))

        //add book to user library in database
        val dataRef3 = databaseRef.child("users/$userId/user_library")
        val childRef3 = dataRef3.push()
        val key3 = childRef3.key.toString()
        dataRef3.child(key3).setValue(FavBook(name, key2, "0", "books/$key2.pdf", "book covers/$key2.jpg", genre, "yes"))

        //storing pdf in firebase storage
        val ref1 = storage.child("books/$key2.pdf")
        ref1.putFile(pdf)

        //storing cover image in firebase storage
        val ref2 = storage.child("book covers/$key2.jpg")
        ref2.putFile(img)
    }

}