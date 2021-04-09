package com.example.kitaab.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Adapter.CommunityAdapter
import com.example.kitaab.Adapter.FavListAdapter
import com.example.kitaab.Model.FavBook
import com.example.kitaab.Model.Genres
import com.example.kitaab.R
import com.example.kitaab.ViewModel.CommunityViewModel

class Community : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        recyclerView = findViewById(R.id.communityRecycler)
        backButton = findViewById(R.id.backButton)

        val commViewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)
        commViewModel.genres.observe(this, Observer {
            setRecycler(it)
        })

        backButton.setOnClickListener {
            this.finish()
        }

    }

    private fun setRecycler(genres: List<String>) {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = CommunityAdapter(this, genres)
    }
}