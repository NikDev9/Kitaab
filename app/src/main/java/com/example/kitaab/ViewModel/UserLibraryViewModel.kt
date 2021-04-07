package com.example.kitaab.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitaab.Model.FavBook
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

                val tempList1: MutableList<FavBook> = ArrayList()
                val tempList2: MutableList<FavBook> = ArrayList()

                val favBooks = snapshot.child("users/$userId/user_library")
                for(snap in favBooks.children) {
                    val book = snap.child("book").value.toString()
                    val bookId = snap.child("bookId").value.toString()
                    val bookmark = snap.child("bookmark").value.toString()
                    val image = snap.child("coverImg").value.toString()
                    val genre = snap.child("genre").value.toString()
                    val filename = snap.child("filename").value.toString()
                    val self = snap.child("self").value.toString()
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

    fun removeFromFav(genre: String, bookId: String) {
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

    fun deleteBook(genre: String, bookId: String, img: String, filename: String) {
        //function call
        removeFromFav(genre,bookId)

        //remove book from firebase database
        val dataRef3 = databaseRef.getReference("books/$genre")
        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    if(snap.key == bookId.toString()) {
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