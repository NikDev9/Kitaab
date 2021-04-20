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
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(activity,3, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = activity?.let { FavListAdapter(it, favList, this) }
    }

    fun showRemoveDialog(genre: String, bookId: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.remove)
        builder.setMessage(R.string.removeAlertMessage)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            userLibViewModel.removeFromFav(genre, bookId)
        }
        builder.setNegativeButton("Cancel"){dialogInterface , which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}