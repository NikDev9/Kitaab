package com.example.kitaab.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Model.BookList
import com.example.kitaab.Model.Genres
import com.example.kitaab.R

class MainLibPageAdapter (private val context: Context, private val allGenres: List<Genres>) : RecyclerView.Adapter<MainLibPageAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var genreTitle: TextView = itemView.findViewById(R.id.genreName)
        var genreBooksRecycler: RecyclerView = itemView.findViewById(R.id.genreBooksRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false))
    }

    override fun getItemCount(): Int {
        return allGenres.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        //set genre title of main library recycler view item
        holder.genreTitle!!.text = allGenres[position].GenreTitle

        //function call to set recycler view items of genres (books of each genre)
        setGenreBookRecycler(holder.genreBooksRecycler, allGenres[position].GenreBookList, allGenres[position].GenreTitle)
    }

    private fun setGenreBookRecycler(recyclerView: RecyclerView, bookList: List<BookList>, genreTitle: String) {
        //adapter to populate books of each genre
        val bookListRecycler = MainLibBookAdapter(context, bookList, genreTitle)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = bookListRecycler
    }

}