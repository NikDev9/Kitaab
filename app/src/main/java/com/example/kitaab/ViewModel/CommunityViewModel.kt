package com.example.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CommunityViewModel : ViewModel() {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    var genres = MutableLiveData<List<String>>()

    init {
        val data = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                val tempList = ArrayList<String>()
                val genreSnapShot = snapshot.child("books/")
                for(snap in genreSnapShot.children)
                    tempList.add(snap.key.toString())
                genres.value = tempList
            }
        }
        databaseRef.addValueEventListener(data)
    }

}