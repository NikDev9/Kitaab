package com.example.kitaab.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Fragments.FavBooksFragment
import com.example.kitaab.Model.FavBook
import com.example.kitaab.R
import com.example.kitaab.View.Read
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class FavListAdapter(private val context: Context, private val favBooks: List<FavBook>, private val favFragment: FavBooksFragment) : RecyclerView.Adapter<FavListAdapter.FavListViewHolder>() {
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
        storageRef.child(favBooks[position].coverImg).downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(holder.bookImg)
        }
        holder.bookImg.setOnClickListener {
            val intent = Intent(context, Read::class.java)
            intent.putExtra("filepath",favBooks[position].filename)
            intent.putExtra("genre",favBooks[position].genre)
            intent.putExtra("bookId",favBooks[position].bookId)
            intent.putExtra("bookmark",favBooks[position].bookmark)
            Log.v("myact","ADAPTER: ${favBooks[position].bookId}")
            context.startActivity(intent)
        }
        holder.removeButton.setOnClickListener {
            favFragment.showRemoveDialog(favBooks[position].genre, favBooks[position].bookId)
        }
    }
}