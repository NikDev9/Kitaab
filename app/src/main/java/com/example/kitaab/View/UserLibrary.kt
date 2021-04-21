package com.nikdev.kitaab.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.kitaab.Adapter.UserLibraryAdapter
import com.example.kitaab.Fragments.FavBooksFragment
import com.example.kitaab.Fragments.PublishedFragment
import com.example.kitaab.R
import com.example.kitaab.ViewModel.UserLibraryViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

class UserLibrary : AppCompatActivity() {

    private lateinit var backButton: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var emptyTabs: TextView
    private lateinit var appBar: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_library)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = ""
        backButton = findViewById(R.id.backButton)
        tabs = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.viewPager)
        emptyTabs = findViewById(R.id.emptyTabs)
        appBar = findViewById(R.id.appBar)

        //adapter to add fragments to view
        val adapter = UserLibraryAdapter(supportFragmentManager)
        var favFragAdded = false
        var publishedFragAdded = false

        //close activity
        backButton.setOnClickListener{
            this.finish()
        }

        //ViewModel of this activity
        val libViewModel = ViewModelProvider(this).get(UserLibraryViewModel::class.java)

        //observe books in user's favourites list
        libViewModel.favList.observe(this, Observer {
            //if favourites fragment is not already present in the view and fav list is not empty, add Favourites fragment
            if(it != null && !favFragAdded) {
                appBar.isVisible = true
                //Hide TextView 'Empty' if list is not empty
                emptyTabs.isVisible = false
                //add fragment
                adapter.addFragment(FavBooksFragment(), "Favourites")
                favFragAdded = true
            }
        })

        //observe books in user's published list
        libViewModel.selfPublishedList.observe(this, Observer {
            //if published fragment is not already present in the view and published list is not empty, add Published fragment
            if(it != null && !publishedFragAdded) {
                appBar.isVisible = true
                //Hide TextView 'Empty' if list is not empty
                emptyTabs.isVisible = false
                //add fragment
                adapter.addFragment(PublishedFragment(), "Published")
                publishedFragAdded = true
            }
            viewPager.adapter = adapter
            //set fragments in tab layout
            tabs.setupWithViewPager(viewPager)
        })
    }

}