package com.example.kitaab.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Model.FavBook
import com.example.kitaab.R
import com.example.kitaab.View.Favourites
import com.example.kitaab.ViewModel.FavListViewModel
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class FavListAdapter (private val context: Context, private val favBooks: List<FavBook>, private val favListView: Favourites) : RecyclerView.Adapter<FavListAdapter.FavListViewHolder>() {
    class FavListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookImg: ImageView = itemView.findViewById(R.id.favBookImage)
        var removeButton: TextView = itemView.findViewById(R.id.removeFromFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavListViewHolder {
        return FavListViewHolder(LayoutInflater.from(context).inflate(R.layout.favlist_recycler_item, parent, false))
    }

    override fun getItemCount(): Int {
        return favBooks.size
    }

    override fun onBindViewHolder(holder: FavListViewHolder, position: Int) {
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        storageRef.child(favBooks[position].filepath).downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(holder.bookImg)
        }
        holder.removeButton.setOnClickListener {
            favListView.showRemoveButton(position)
        }
    }
}