package com.nikdev.kitaab.View

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.nikdev.kitaab.R
import com.nikdev.kitaab.ViewModel.ReadViewModel
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class Read : AppCompatActivity() {

    private lateinit var pdfViewer: PDFView
    private lateinit var readViewModel: ReadViewModel
    private lateinit var genre: String
    private lateinit var bookId: String
    private lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        pdfViewer = findViewById(R.id.pdfView)
        progressBar = findViewById(R.id.readProgressBar)
        genre = intent.getStringExtra("genre").toString()
        bookId = intent.getStringExtra("bookId").toString()
        val filePathName = intent.getStringExtra("filepath").toString()
        val defaultPage = intent.getStringExtra("bookmark").toString()
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child(filePathName)

        readViewModel = ViewModelProvider(this).get(ReadViewModel::class.java)
        readViewModel.setVariables(genre, bookId)

        val pageNo = defaultPage.toInt()
        val localFile = File.createTempFile("book", "pdf")
        storageRef.getFile(localFile).addOnSuccessListener {
            pdfViewer.fromFile(localFile)
                    .defaultPage(pageNo)
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .load()
            progressBar.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val page = pdfViewer.currentPage
        readViewModel.markPage(page)
    }
}