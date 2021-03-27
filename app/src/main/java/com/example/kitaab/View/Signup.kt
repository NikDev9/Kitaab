package com.example.kitaab.View

import android.content.Intent
import android.os.Bundle

import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kitaab.R
import com.example.kitaab.ViewModel.SignupViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class Signup : AppCompatActivity() {

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
        val SignupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

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
                    SignupViewModel.SignUp(email.text.toString(), pass1.text.toString(), fullname.text.toString())
                    SignupViewModel.returnVal.observe(this, Observer {
                        if(it == "success") {
                            val intent = Intent(this, Library::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            progress.isVisible = false
                            buttonSignUp.isEnabled = true
                            Toast.makeText(this,"$it", LENGTH_SHORT).show()
                        }
                    })
                }
            }
            else
                Toast.makeText(this, "Please fill out all the details", LENGTH_SHORT).show()
        }
    }
}