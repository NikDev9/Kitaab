package com.example.kitaab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Signup : AppCompatActivity() {

    private val TAG = "MyActivity"

    private lateinit var buttonLogin: TextView
    private lateinit var buttonSignUp: Button
    private lateinit var email: EditText
    private lateinit var fullname: EditText
    private lateinit var pass1: EditText
    private lateinit var pass2: EditText
    private lateinit var progress: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dataRef: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()

        buttonLogin = findViewById(R.id.back_to_login)
        buttonSignUp = findViewById(R.id.sign_up)
        email = findViewById(R.id.reg_email)
        fullname = findViewById(R.id.full_name)
        pass1 = findViewById(R.id.password1)
        pass2 = findViewById(R.id.password2)
        progress = findViewById(R.id.progressBar)

        buttonLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        buttonSignUp.setOnClickListener{

            if(email.text.toString().isNotEmpty() && fullname.text.toString().isNotEmpty() && pass1.text.toString().isNotEmpty() && pass2.text.toString().isNotEmpty())
            {
                if(pass1.text.toString() != pass2.text.toString())
                    Toast.makeText(this,"Both passwords do not match", LENGTH_SHORT).show()
                else
                {
                    progress.isVisible = true
                    buttonSignUp.isEnabled = false
                    mAuth.createUserWithEmailAndPassword(email.text.toString(), pass1.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful)
                            {
                                userId = mAuth.currentUser!!.uid
                                dataRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)
                                val userHashMap = HashMap<String, Any>()
                                userHashMap["id"] = userId
                                userHashMap["fullname"] = fullname.text.toString()
                                userHashMap["email"] = email.text.toString()
                                userHashMap["image"] = "gs://kitaab-1f50d.appspot.com/profile_picture.jpg"
                                userHashMap["description"] = ""

                                dataRef.updateChildren(userHashMap)
                                    .addOnCompleteListener { task ->
                                        if(task.isSuccessful)
                                        {
                                            val intent = Intent(this, Library::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        else
                                        {
                                            progress.isVisible = false
                                            buttonSignUp.isEnabled = true
                                            val errorMsg = task.exception
                                            Toast.makeText(this,"$errorMsg", LENGTH_SHORT).show()
                                        }
                                    }
                            }
                            else
                            {
                                progress.isVisible = false
                                buttonSignUp.isEnabled = true
                                val errorMsg = task.exception
                                val index = errorMsg.toString().indexOf(':')
                                val domain: String? = if (index == -1) null else errorMsg.toString().substring(index + 1)
                                Toast.makeText(this,"$domain", LENGTH_SHORT).show()
                            }
                        }
                }
            }
            else
                Toast.makeText(this, "Please fill out all the details", LENGTH_SHORT).show()
        }
    }
}