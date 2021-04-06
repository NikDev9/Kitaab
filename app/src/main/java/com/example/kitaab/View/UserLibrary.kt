package com.example.kitaab.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.kitaab.Adapter.BooksViewPagerAdapter
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
        val adapter = BooksViewPagerAdapter(supportFragmentManager)
        var favFragAdded = false
        var publishedFragAdded = false

        backButton.setOnClickListener{
            this.finish()
        }

        val libViewModel = ViewModelProvider(this).get(UserLibraryViewModel::class.java)

        libViewModel.favList.observe(this, Observer {
            if(it != null && !favFragAdded) {
                appBar.isVisible = true
                emptyTabs.isVisible = false
                adapter.addFragment(FavBooksFragment(), "Favourites")
                favFragAdded = true
            }
        })
        libViewModel.selfPublishedList.observe(this, Observer {
            if(it != null && !publishedFragAdded) {
                appBar.isVisible = true
                emptyTabs.isVisible = false
                adapter.addFragment(PublishedFragment(), "Published")
                publishedFragAdded = true
            }
            viewPager.adapter = adapter
            tabs.setupWithViewPager(viewPager)
        })
    }

}