package com.nikdev.kitaab.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nikdev.kitaab.Model.BookReviews
import com.nikdev.kitaab.R

class ReviewsAdapter (private val context: Context, private val allReviews: List<BookReviews>) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var review: TextView = itemView.findViewById(R.id.review)
        var reviewer: TextView = itemView.findViewById(R.id.reviewer)
        var rating: RatingBar = itemView.findViewById(R.id.uRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_review, parent, false))
    }

    override fun getItemCount(): Int {
        return allReviews.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        //set review, reviewer's name and rating in reviews recycler view
        holder.review!!.text = allReviews[position].review
        holder.reviewer!!.text = allReviews[position].reviewer
        holder.rating.rating = allReviews[position].rating.toFloat()
    }

}