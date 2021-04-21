package com.nikdev.kitaab.Fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikdev.kitaab.Adapter.PublishedListAdapter
import com.nikdev.kitaab.Model.FavBook
import com.nikdev.kitaab.R
import com.nikdev.kitaab.ViewModel.UserLibraryViewModel

class PublishedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userLibViewModel: UserLibraryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ViewModel of UserLibrary activity
        userLibViewModel = ViewModelProvider(this).get(UserLibraryViewModel::class.java)
        userLibViewModel.selfPublishedList.observe(this, Observer {
            setRecycler(it)
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_published, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.PublishedRecycler)
    }

    private fun setRecycler(publishedList: List<FavBook>) {
        //populate published list recycler view in grid layout by calling PublishedListAdapter
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(activity,3, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = activity?.let { PublishedListAdapter(it, publishedList, this) }
    }

    //called when user's wants to delete self published book
    fun showAlertDialog(genre: String, bookId: String, image: String, filename: String) {

        //alert message to check whether user surely wants to delete from database
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.deleteAlertMessage)

        //if yes then remove view model's function is called to delete from database
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            userLibViewModel.deleteBook(genre, bookId, image, filename)
        }
        //do nothing
        builder.setNegativeButton("Cancel"){dialogInterface , which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}