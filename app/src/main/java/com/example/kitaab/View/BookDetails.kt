package com.nikdev.kitaab.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikdev.kitaab.Adapter.ReviewsAdapter
import com.nikdev.kitaab.Model.BookReviews
import com.nikdev.kitaab.R
import com.nikdev.kitaab.ViewModel.BookDetailsViewModel
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class BookDetails : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var author: TextView
    private lateinit var bookName: TextView
    private lateinit var synopsis: TextView
    private lateinit var image: ImageView
    private lateinit var backButton: TextView
    private lateinit var headingTextView: TextView
    private lateinit var addToFavButton: Button
    private lateinit var addReviewButton: Button
    private lateinit var typedReview: EditText
    private lateinit var sendReview: Button
    private lateinit var rating: RatingBar
    private lateinit var ratingNo: TextView
    private lateinit var userRating: RatingBar
    private lateinit var revTextBar: RelativeLayout
    private lateinit var closeRevBar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        // to show back button in action bar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = ""
        author = findViewById(R.id.author)
        bookName = findViewById(R.id.bookTitle)
        synopsis = findViewById(R.id.synopsis)
        image = findViewById(R.id.bookCoverImg)
        backButton = findViewById(R.id.backButton)
        addToFavButton = findViewById(R.id.addBooktoFav)
        addReviewButton = findViewById(R.id.addRevBtn)
        typedReview = findViewById(R.id.typedReview)
        sendReview = findViewById(R.id.sendReview)
        revTextBar = findViewById(R.id.revTextBar)
        rating = findViewById(R.id.rating)
        ratingNo = findViewById(R.id.ratingNo)
        userRating = findViewById(R.id.userRating)
        headingTextView = findViewById(R.id.HeadingTextView)
        closeRevBar = findViewById(R.id.closeRevBar)

        val genre = intent.getStringExtra("genre").toString()
        val bookId = intent.getStringExtra("bookId").toString()

        //ViewModel of this activity
        val bookDetailsViewModel = ViewModelProvider(this).get(BookDetailsViewModel::class.java)

        //send genre and bookId passed by previous activity to ViewModel
        bookDetailsViewModel.setDetails(genre,bookId)

        //display reviews by calling function
        bookDetailsViewModel.reviews.observe(this, Observer{
            setMainRecycler(it)
        })
        //display author's name
        bookDetailsViewModel.author.observe(this, Observer{
            author.text = it
        })
        //display synopsis
        bookDetailsViewModel.synopsis.observe(this, Observer{
            synopsis.text = it
        })
        //display book name
        bookDetailsViewModel.name.observe(this, Observer{
            bookName.text = it
        })
        //display overall rating
        bookDetailsViewModel.rating.observe(this, Observer{
            rating.rating = it.toFloat()
            ratingNo.text = it
        })
        //display heading 'User reviews' only if there is atleast one review
        bookDetailsViewModel.hideTextView.observe(this, Observer{
            headingTextView.isVisible = it
        })
        //If book already in fav list, show 'add review' button, change text of 'add to fav' button and disable it
        bookDetailsViewModel.buttonText.observe(this, Observer{
            addToFavButton.text = it
            addToFavButton.isEnabled = false
            addReviewButton.isVisible = true
            Toast.makeText(this, "Go to your library to start reading",Toast.LENGTH_LONG).show()
        })
        //load book image from download url
        bookDetailsViewModel.image.observe(this, Observer{
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            storageRef.child(it).downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(image)
            }
        })

        //close activity
        backButton.setOnClickListener{
            this.finish()
        }
        //add book to favourites list
        addToFavButton.setOnClickListener{
            bookDetailsViewModel.addtoFav()
            addToFavButton.isEnabled = false
        }
        //display layout to type book review and add rating
        addReviewButton.setOnClickListener {
            revTextBar.isVisible = true
        }
        //add review and rating to database
        sendReview.setOnClickListener {
            //both rating and review are required
            if(typedReview.text.isEmpty() || userRating.rating.isNaN())
                Toast.makeText(this, "Please fill all the details",Toast.LENGTH_SHORT).show()
            else {
                //function call
                bookDetailsViewModel.addReview(typedReview.text.toString(), userRating.rating)
                //hide layout of adding review and rating
                revTextBar.isVisible = false
                Toast.makeText(this, "Your review has been added",Toast.LENGTH_SHORT).show()
                headingTextView.isVisible = true
                typedReview.text = null
            }
        }
        //when user cancels action of adding review and rating
        closeRevBar.setOnClickListener {
            revTextBar.isVisible = false
        }

    }

    //set recycler view of reviews
    private fun setMainRecycler(Reviews: List<BookReviews>) {
        recyclerView = findViewById(R.id.reviewRecycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = ReviewsAdapter(this,Reviews)
    }

}