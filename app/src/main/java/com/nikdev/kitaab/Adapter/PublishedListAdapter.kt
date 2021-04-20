package com.nikdev.kitaab.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nikdev.kitaab.Fragments.PublishedFragment
import com.nikdev.kitaab.Model.FavBook
import com.nikdev.kitaab.R
import com.nikdev.kitaab.View.Read
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class PublishedListAdapter(private val context: Context, private val pubBooks: List<FavBook>, private val fragment: PublishedFragment) : RecyclerView.Adapter<PublishedListAdapter.FavListViewHolder>() {
    class FavListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookImg: ImageView = itemView.findViewById(R.id.publishedBookImage)
        var delButton: TextView = itemView.findViewById(R.id.delFromMainLib)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavListViewHolder {
        return FavListViewHolder(LayoutInflater.from(context).inflate(R.layout.published_recycler_item, parent, false))
    }

    override fun getItemCount(): Int {
        return pubBooks.size
    }

    override fun onBindViewHolder(holder: FavListViewHolder, position: Int) {
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        storageRef.child(pubBooks[position].coverImg).downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(holder.bookImg)
        }
        holder.bookImg.setOnClickListener {
            val intent = Intent(context, Read::class.java)
            intent.putExtra("filepath",pubBooks[position].filename)
            intent.putExtra("genre",pubBooks[position].genre)
            intent.putExtra("bookId",pubBooks[position].bookId)
            intent.putExtra("bookmark",pubBooks[position].bookmark)
            context.startActivity(intent)
        }
        holder.delButton.setOnClickListener {
            fragment.showAlertDialog(pubBooks[position].genre, pubBooks[position].bookId, pubBooks[position].coverImg, pubBooks[position].filename)
        }
    }
}