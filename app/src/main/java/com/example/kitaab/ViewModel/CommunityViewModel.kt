package com.nikdev.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class CommunityViewModel : ViewModel() {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    var genres = MutableLiveData<List<String>>()

    init {
        val data = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                //retrieves community channel names
                val tempList = ArrayList<String>()
                val commSnapShot = snapshot.child("community")
                for(snap in commSnapShot.children)
                    tempList.add(snap.child("name").value.toString())
                genres.value = tempList
            }
        }
        databaseRef.addValueEventListener(data)
    }

}