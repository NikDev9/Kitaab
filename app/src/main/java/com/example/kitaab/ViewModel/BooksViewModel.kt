package com.example.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitaab.Model.BookList
import com.example.kitaab.Model.Genres
import com.example.kitaab.Repository.DataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class BooksViewModel: ViewModel() {

    var allGenres = MutableLiveData<MutableList<Genres>>()
    var userImage = MutableLiveData<String>()
    var userName = MutableLiveData<String>()

    init {

        val repo = DataRepository()
        allGenres = repo.getGenres()
        userName = repo.getUsername()
        userImage = repo.getUserimage()

    }

}