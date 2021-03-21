package com.example.kitaab.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Model.BookList
import com.example.kitaab.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class HorizontalRecyclerAdapter (private val context: Context, private val bookList: List<BookList>) : RecyclerView.Adapter<HorizontalRecyclerAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookTitle: TextView = itemView.findViewById(R.id.bookName)
        var bookImg: ImageButton = itemView.findViewById(R.id.bookImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_row_item, parent, false))
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bookTitle!!.text = bookList[position].bookName
         val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        storageRef.child(bookList[position].bookImage).downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(holder.bookImg)
        }
    }

}