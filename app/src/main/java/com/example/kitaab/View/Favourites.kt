package com.example.kitaab.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Adapter.FavListAdapter
import com.example.kitaab.Adapter.ReviewsAdapter
import com.example.kitaab.Model.FavBook
import com.example.kitaab.R
import com.example.kitaab.ViewModel.BookDetailsViewModel
import com.example.kitaab.ViewModel.FavListViewModel

class Favourites : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = ""
        backButton = findViewById(R.id.backButton)
        val favListViewModel = ViewModelProvider(this).get(FavListViewModel::class.java)

        favListViewModel.favList.observe(this, Observer {
            setRecycler(it)
        })
        backButton.setOnClickListener{
            this.finish()
        }

    }

    private fun setRecycler(favList: List<FavBook>) {
        recyclerView = findViewById(R.id.FavListRecycler)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this,3,LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = FavListAdapter(this,favList)
    }

}