package com.nikdev.kitaab.View

import android.content.Intent
import android.os.Bundle
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
import com.nikdev.kitaab.Adapter.RecyclerViewAdapter
import com.nikdev.kitaab.Model.Genres
import com.nikdev.kitaab.R
import com.nikdev.kitaab.ViewModel.BooksViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class Library : AppCompatActivity() {

    private lateinit var profilePic: ImageView
    private lateinit var userName: TextView
    private lateinit var signOutButton: TextView
    private lateinit var goToFav: TextView
    private lateinit var goToHome: TextView
    private lateinit var addStory: TextView
    private lateinit var community: TextView
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
        goToFav = findViewById(R.id.goToFav)
        goToHome= findViewById(R.id.goToHome)
        addStory = findViewById(R.id.addStory)
        community = findViewById(R.id.community)
        storage = FirebaseStorage.getInstance()
        drawer = findViewById(R.id.drawer)
        var genreTitles: MutableList<Genres>

        //ViewModel of this activity
        val mainLibViewModel = ViewModelProvider(this).get(MainLibraryViewModel::class.java)

        //get all genres from database and call function to set recycler view
        mainLibViewModel.allGenres.observe(this, Observer {
            genreTitles = it
            setMainRecycler(genreTitles)
        })
        //load user profile pic from download URL in the navigation menu
        mainLibViewModel.userImage.observe(this, Observer {
            val storageRef = storage.reference
            storageRef.child(it).downloadUrl.addOnSuccessListener { it1 ->
                Picasso.get().load(it1).into(profilePic)
            }
        })
        //display username
        mainLibViewModel.userName.observe(this, Observer {
            userName.text = "Hey $it !"
        })

        //(menu item) user sign out
        signOutButton.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        //(menu item) open user library
        goToFav.setOnClickListener {
            val intent = Intent(this, UserLibrary::class.java)
            startActivity(intent)
        }
        //(menu item) open page for user to publish story
        addStory.setOnClickListener {
            val intent = Intent(this, AddBook::class.java)
            startActivity(intent)
        }
        //(menu item) open community page
        community.setOnClickListener {
            val intent = Intent(this, Community::class.java)
            startActivity(intent)
        }
        //close drawer (menu)
        goToHome.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
        }

    }

    //set genre recycler view adapter and layout (genres are populated vertically in main library)
    private fun setMainRecycler(Genres: List<Genres>) {
        recyclerView = findViewById(R.id.main_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = MainLibPageAdapter(this,Genres)
        progress.isVisible = false
    }

    //open drawer (menu)
    fun openMenu(view: View) = drawer.openDrawer(GravityCompat.START)

}