package com.example.kitaab.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Adapter.RecyclerViewAdapter
import com.example.kitaab.Model.Genres
import com.example.kitaab.R
import com.example.kitaab.ViewModel.BooksViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class Library : AppCompatActivity() {

    private val TAG = "MyActivity"

    private lateinit var profilePic: ImageView
    private lateinit var userName: TextView
    private lateinit var signOutButton: TextView
    private lateinit var goToFav: TextView
    private lateinit var progress: RelativeLayout
    private lateinit var drawer: DrawerLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        mAuth = FirebaseAuth.getInstance()
        progress = findViewById(R.id.progress)
        profilePic = findViewById(R.id.picture)
        userName = findViewById(R.id.userName)
        signOutButton = findViewById(R.id.signOutButton)
        goToFav = findViewById(R.id.GotoFav)
        storage = FirebaseStorage.getInstance()
        drawer = findViewById(R.id.drawer)

        val bookViewModel = ViewModelProvider(this).get(BooksViewModel::class.java)

        var genreTitles: MutableList<Genres>

        bookViewModel.allGenres.observe(this, Observer {
            genreTitles = it
            setMainRecycler(genreTitles)
        })
        bookViewModel.userImage.observe(this, Observer {
            val storageRef = storage.reference
            storageRef.child(it).downloadUrl.addOnSuccessListener { it1 ->
                Picasso.get().load(it1).into(profilePic)
            }
        })
        bookViewModel.userName.observe(this, Observer {
            userName.text = "Hey $it !"
        })

        signOutButton.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        goToFav.setOnClickListener {
            val intent = Intent(this, Favourites::class.java)
            startActivity(intent)
        }

    }

    private fun setMainRecycler(Genres: List<Genres>) {
        recyclerView = findViewById(R.id.main_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = RecyclerViewAdapter(this,Genres)
        progress.isVisible = false
    }

    fun openMenu(view: View) = drawer.openDrawer(GravityCompat.START)

}