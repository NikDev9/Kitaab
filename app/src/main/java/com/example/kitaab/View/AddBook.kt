package com.example.kitaab.View

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kitaab.R
import com.example.kitaab.ViewModel.AddBookViewModel
import android.net.Uri


class AddBook : AppCompatActivity() {

    private lateinit var publishButton: Button
    private lateinit var uploadPdf: Button
    private lateinit var uploadImage: Button
    private lateinit var backButton: TextView
    private lateinit var bookName: EditText
    private lateinit var synopsis: EditText
    private lateinit var spinner: Spinner
    private lateinit var imgUri: Uri
    private lateinit var pdfUri: Uri
    private lateinit var addViewModel: AddBookViewModel

    val PHOTO_REQUEST = 1
    val FILE_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        publishButton = findViewById(R.id.publishButton)
        uploadPdf = findViewById(R.id.uploadPdfbutton)
        uploadImage = findViewById(R.id.uploadImageButton)
        backButton = findViewById(R.id.backButton)
        bookName = findViewById(R.id.userBookName)
        synopsis = findViewById(R.id.publishSynopsis)
        spinner = findViewById(R.id.spinner)
        pdfUri = Uri.EMPTY
        imgUri = Uri.EMPTY

        //ViewModel of this activity
        addViewModel = ViewModelProvider(this).get(AddBookViewModel::class.java)

        //populate dropdown/spinner in the layout
        addViewModel.genres.observe(this, Observer {
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        })

        //to let user select pdf from the device
        uploadPdf.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(Intent.createChooser(intent,"Choose pdf"),FILE_REQUEST)
        }

        //to let user select image from the device
        uploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"Choose cover image"),PHOTO_REQUEST)
        }

        //upload book to the database and store files on firebase storage
        publishButton.setOnClickListener{
            //check if any field is left empty
            if(pdfUri == Uri.EMPTY || imgUri == Uri.EMPTY || bookName.text.toString() == "" || synopsis.text.toString() == "")
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            else
                publishBook()
        }

        //close activity
        backButton.setOnClickListener {
            this.finish()
        }

    }

    //callback after user has selected a file from the device
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null) {
            //selected file is of pdf format
            if (requestCode == FILE_REQUEST) {
                //set returned Uri from the device
                pdfUri = data.data!!
                uploadPdf.text = "PDF selected"
            }
            //selected file is an image
            if (requestCode == PHOTO_REQUEST) {
                //set returned Uri from the device
                imgUri = data.data!!
                uploadImage.text = "Image selected"
            }
        }
    }

    //called when all fields are filled up and publish button is clicked. It uploads book to the database.
    fun publishBook() {
        //ask for confirmation
        val selected: String = spinner.selectedItem.toString()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.publish)
        builder.setMessage(R.string.publishAlertMessage)
        //if yes, publish book
        builder.setPositiveButton("Publish"){dialogInterface, which ->
            addViewModel.uploadFiles(pdfUri, imgUri, bookName.text.toString(), synopsis.text.toString(), selected)
            val intent = Intent(this,UserLibrary::class.java)
            startActivity(intent)
            this.finish()
        }
        //do nothing
        builder.setNegativeButton("Cancel"){dialogInterface , which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}