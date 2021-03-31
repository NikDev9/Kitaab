package com.example.kitaab.View

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Adapter.FavListAdapter
import com.example.kitaab.Model.FavBook
import com.example.kitaab.R
import com.example.kitaab.ViewModel.FavListViewModel

class Favourites : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: TextView
    private lateinit var favListViewModel: FavListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = ""

        backButton = findViewById(R.id.backButton)
        favListViewModel = ViewModelProvider(this).get(FavListViewModel::class.java)

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
        recyclerView!!.adapter = FavListAdapter(this,favList, favListView = this)
    }

    fun showRemoveButton(key: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.remove)
        builder.setMessage(R.string.removeAlertMessage)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            favListViewModel.removeFromFav(key)
        }
        builder.setNegativeButton("Cancel"){dialogInterface , which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}