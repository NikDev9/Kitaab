package com.nikdev.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikdev.kitaab.Model.FavBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class UserLibraryViewModel : ViewModel() {

    private val databaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dataRef: DatabaseReference = databaseRef.reference
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId = mAuth.currentUser!!.uid
    var favList = MutableLiveData<List<FavBook>>()
    var selfPublishedList = MutableLiveData<List<FavBook>>()

    init {

        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                //contains user's favourites list of books (data object of FavBook)
                val tempList1: MutableList<FavBook> = ArrayList()
                //contains books uploaded by the user (data object of FavBook)
                val tempList2: MutableList<FavBook> = ArrayList()

                //retrieve book details from user library
                val favBooks = snapshot.child("users/$userId/user_library")
                for(snap in favBooks.children) {
                    val book = snap.child("book").value.toString()
                    val bookId = snap.child("bookId").value.toString()
                    val bookmark = snap.child("bookmark").value.toString()
                    val image = snap.child("coverImg").value.toString()
                    val genre = snap.child("genre").value.toString()
                    val filename = snap.child("filename").value.toString()
                    val self = snap.child("self").value.toString()
                    //self yes - books published by user, no - added from main library to fav list
                    if(self == "no")
                        tempList1.add(FavBook(book, bookId, bookmark, filename, image, genre, self))
                    else
                        tempList2.add(FavBook(book, bookId, bookmark, filename, image, genre, self))
                }
                favList.value = tempList1
                selfPublishedList.value = tempList2
            }
        }
        dataRef.addValueEventListener(data)

    }

    //called when user removes a book from fav list
    fun removeFromFav(genre: String, bookId: String) {
        val dataRef2 = databaseRef.getReference("users/$userId/user_library")
        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    //a book is uniquely identified by its genre and id and removed from user library
                    if(snap.child("genre").value == genre && snap.child("bookId").value == bookId) {
                        val ref = databaseRef.getReference("users/$userId/user_library")
                        ref.child("${snap.key}").removeValue()
                    }
                }
            }
        }
        dataRef2.addValueEventListener(data)
    }

    //called when user wants to delete published book from database
    fun deleteBook(genre: String, bookId: String, img: String, filename: String) {

        //function call to remove book from user library
        removeFromFav(genre,bookId)

        //to remove book from main library
        val dataRef3 = databaseRef.getReference("books/$genre")
        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    if(snap.key == bookId) {
                        val ref = databaseRef.getReference("books/$genre")
                        ref.child("$bookId").removeValue()
                    }
                }
            }
        }
        dataRef3.addValueEventListener(data)

        //Delete files from firebase storage
        val imageRef = storageRef.child(img)
        val fileRef = storageRef.child(filename)
        imageRef.delete()
        fileRef.delete()
    }

}