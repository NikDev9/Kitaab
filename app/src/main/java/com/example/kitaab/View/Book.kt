package com.example.kitaab.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Adapter.ReviewsAdapter
import com.example.kitaab.Model.BookReviews
import com.example.kitaab.R
import com.example.kitaab.ViewModel.BookDetailsViewModel
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class Book : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var author: TextView
    private lateinit var bookName: TextView
    private lateinit var synopsis: TextView
    private lateinit var image: ImageView
    private lateinit var backButton: TextView
    private lateinit var headingTextView: TextView
    private lateinit var addToFavButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        // showing the back button in action bar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = ""
        author = findViewById(R.id.author)
        bookName = findViewById(R.id.bookTitle)
        synopsis = findViewById(R.id.synopsis)
        image = findViewById(R.id.bookCoverImg)
        backButton = findViewById(R.id.backButton)
        addToFavButton = findViewById(R.id.addBooktoFav)
        headingTextView = findViewById(R.id.HeadingTextView)

        val genre = intent.getStringExtra("genre").toString()
        val bookId = intent.getStringExtra("bookId").toString()

        val bookDetailsViewModel = ViewModelProvider(this).get(BookDetailsViewModel::class.java)
        bookDetailsViewModel.setDetails(genre,bookId)
        bookDetailsViewModel.reviews.observe(this, Observer{
            setMainRecycler(it)
        })
        bookDetailsViewModel.author.observe(this, Observer{
            author.text = it
        })
        bookDetailsViewModel.synopsis.observe(this, Observer{
            synopsis.text = it
        })
        bookDetailsViewModel.name.observe(this, Observer{
            bookName.text = it
        })
        bookDetailsViewModel.hideTextView.observe(this, Observer{
            headingTextView.isVisible = it
        })
        bookDetailsViewModel.buttonText.observe(this, Observer{
            addToFavButton.text = it
            addToFavButton.isEnabled = false
        })
        bookDetailsViewModel.image.observe(this, Observer{
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            storageRef.child(it).downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(image)
            }
        })

        backButton.setOnClickListener{
            this.finish()
        }
        addToFavButton.setOnClickListener{
            bookDetailsViewModel.addtoFav()
            this.addToFavButton.isEnabled = false
        }

    }

    private fun setMainRecycler(Reviews: List<BookReviews>) {
        recyclerView = findViewById(R.id.reviewRecycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = ReviewsAdapter(this,Reviews)
    }
}