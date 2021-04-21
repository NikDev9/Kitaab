package com.nikdev.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikdev.kitaab.Model.BookReviews
import com.nikdev.kitaab.Model.FavBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookDetailsViewModel : ViewModel() {

    private val databaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dataRef: DatabaseReference = databaseRef.reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var author = MutableLiveData<String>()
    var rating = MutableLiveData<String>()
    var synopsis = MutableLiveData<String>()
    var image = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var reviews = MutableLiveData<List<BookReviews>>()
    var buttonText = MutableLiveData<String>()
    var hideTextView = MutableLiveData<Boolean>()
    lateinit var bookId: String
    lateinit var genre: String
    lateinit var filename: String
    lateinit var username: String
    var ratingSum: Float = 0F
    var totalRatings = 0
    val userId = mAuth.currentUser!!.uid

    fun setDetails(gen: String, id: String) {
        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                genre = gen
                bookId = id

                val userId = mAuth.currentUser.uid
                username = snapshot.child("users/$userId/fullname").value.toString()

                //to check if book is already in user's fav list
                val favBooks = snapshot.child("users/$userId/user_library")
                for(snap in favBooks.children) {
                    //if book is already in fav list, button text 'Add to fav' is changed and 'add review' button is shown to the user
                    if(snap.child("genre").value == genre && snap.child("bookId").value == bookId) {
                        buttonText.value = "Added to Favourites"
                    }
                }

                //to retrieve book details and stores them in livedata to be displayed via view
                val tempList : MutableList<BookReviews> = ArrayList()
                val books = snapshot.child("books/$genre/$bookId")
                author.value = books.child("author").value.toString()
                rating.value = books.child("overall rating").value.toString()
                synopsis.value = books.child("synopsis").value.toString()
                image.value = books.child("coverImg").value.toString()
                name.value = books.child("name").value.toString()
                filename = books.child("filename").value.toString()

                //to retrieve all reviews and ratings of the book
                val reviewsSnap = books.child("reviews")
                if(!reviewsSnap.hasChildren())
                    hideTextView.value = false
                for(snap in reviewsSnap.children) {
                    val review = snap.child("review").value.toString()
                    val reviewer = snap.child("reviewer").value.toString()
                    val rating = snap.child("rating").value.toString()
                    //list of BookReviews data objects
                    tempList.add(BookReviews(review,reviewer,rating))
                    ratingSum += rating.toFloat()
                    totalRatings++
                }
                reviews.value = tempList
            }
        }
        dataRef.addValueEventListener(data)
    }

    //called when user clicks on 'add to favourites', book is added to user's library
    fun addtoFav() {
        val dataRef2 = dataRef.child("users/$userId/user_library")
        val newChildRef = dataRef2.push()
        val key = newChildRef.key.toString()
        dataRef2.child(key).setValue(FavBook("${name.value}", bookId, "1", filename, "${image.value}", genre, "no"))
    }

    //called when user adds review and rating
    fun addReview(review: String, userRating: Float) {
        //add review to database
        val dataRef2 = dataRef.child("books/$genre/$bookId/reviews")
        val childRef = dataRef2.push()
        val key = childRef.key.toString()
        dataRef2.child(key).setValue(BookReviews(review, username, userRating.toString()))

        //to calculate average of user's rating and the rest of the ratings present in the database and add to database
        val calcRating = (ratingSum+userRating)/(totalRatings+1)
        val rating = "%.1f".format(calcRating)
        dataRef.child("books/$genre/$bookId/overall rating").setValue(rating)
    }

}