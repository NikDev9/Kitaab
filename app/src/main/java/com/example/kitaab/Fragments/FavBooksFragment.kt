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
import com.nikdev.kitaab.Adapter.FavListAdapter
import com.nikdev.kitaab.Model.FavBook
import com.nikdev.kitaab.R
import com.nikdev.kitaab.ViewModel.UserLibraryViewModel

class FavBooksFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userLibViewModel: UserLibraryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ViewModel of UserLibrary activity
        userLibViewModel = ViewModelProvider(this).get(UserLibraryViewModel::class.java)
        userLibViewModel.favList.observe(this, Observer {
            setRecycler(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.FavListRecycler)
    }

    private fun setRecycler(favList: List<FavBook>) {
        //populate favourites list recycler view in grid layout by calling FavListAdapter
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(activity,3, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = activity?.let { FavListAdapter(it, favList, this) }
    }

    //called when user wants to remove book from favourites list
    fun showRemoveDialog(genre: String, bookId: String) {

        //alert message to check whether user surely wants to remove from list
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.remove)
        builder.setMessage(R.string.removeAlertMessage)

        //if yes then remove view model's function is called to remove
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            userLibViewModel.removeFromFav(genre, bookId)
        }
        //do nothing
        builder.setNegativeButton("Cancel"){dialogInterface , which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}