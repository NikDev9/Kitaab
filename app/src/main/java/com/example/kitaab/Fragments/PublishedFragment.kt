package com.example.kitaab.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaab.Adapter.FavListAdapter
import com.example.kitaab.Adapter.PublishedListAdapter
import com.example.kitaab.Model.FavBook
import com.example.kitaab.R
import com.example.kitaab.ViewModel.UserLibraryViewModel

class PublishedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userLibViewModel: UserLibraryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(activity,3, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = activity?.let { PublishedListAdapter(it, publishedList, this) }
    }

    fun showAlertDialog(genre: String, bookId: String, image: String, filename: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.deleteAlertMessage)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            userLibViewModel.deleteBook(genre, bookId, image, filename)
        }
        builder.setNegativeButton("Cancel"){dialogInterface , which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}