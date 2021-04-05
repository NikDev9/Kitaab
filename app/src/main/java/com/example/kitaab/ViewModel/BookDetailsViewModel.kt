package com.example.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitaab.Model.BookReviews
import com.example.kitaab.Model.FavBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BookDetailsViewModel : ViewModel() {

    private val databaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dataRef: DatabaseReference = databaseRef.reference
    var author = MutableLiveData<String>()
    var rating = MutableLiveData<Long>()
    var synopsis = MutableLiveData<String>()
    var image = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var reviews = MutableLiveData<List<BookReviews>>()
    var buttonText = MutableLiveData<String>()
    var hideTextView = MutableLiveData<Boolean>()
    var numBooks: Long = 0
    lateinit var bookId: String
    lateinit var genre: String
    lateinit var filename: String

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId = mAuth.currentUser!!.uid

    fun setDetails(gen: String, id: String) {
        val data = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {

                genre = gen
                bookId = id

                //to get the number of books in user's fav list
                numBooks = snapshot.child("users/$userId/bookNum").value as Long

                //to check if book is already in user's fav list
                val favBooks = snapshot.child("users/$userId/user_library")
                for(snap in favBooks.children) {
                    if(snap.child("genre").value == genre && snap.child("bookId").value == bookId.toLong())
                        buttonText.value = "Added to Favourites"
                }

                val tempList : MutableList<BookReviews> = ArrayList()
                val books = snapshot.child("books/$genre/$bookId")
                author.value = books.child("author").value.toString()
                rating.value = books.child("rating").value as Long?
                synopsis.value = books.child("synopsis").value.toString()
                image.value = books.child("coverImg").value.toString()
                name.value = books.child("name").value.toString()
                filename = books.child("filename").value.toString()

                val reviewsSnap = books.child("reviews")
                if(!reviewsSnap.hasChildren())
                    hideTextView.value = false
                for(snap in reviewsSnap.children) {
                    val review = snap.child("review").value.toString()
                    val reviewer = snap.child("username").value.toString()
                    val rating = snap.child("rating").value.toString()
                    tempList.add(BookReviews(review,reviewer,rating))
                }
                reviews.value = tempList
            }
        }
        dataRef.addValueEventListener(data)
    }

    fun addtoFav() {
        val dataRef2 = FirebaseDatabase.getInstance().reference.child("users/$userId")
        dataRef2.child("user_library/$numBooks").setValue(FavBook("${name.value}", bookId.toLong(), "1", filename, "${image.value}", genre)).addOnCompleteListener { task ->
        }
        //increment number of fav books of the user in the database
        dataRef2.child("bookNum").setValue(numBooks+1).addOnCompleteListener { task ->
        }
    }

}