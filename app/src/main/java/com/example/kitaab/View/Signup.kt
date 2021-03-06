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


class Signup : AppCompatActivity() {

    private lateinit var buttonLogin: TextView
    private lateinit var buttonSignUp: Button
    private lateinit var email: EditText
    private lateinit var fullname: EditText
    private lateinit var pass1: EditText
    private lateinit var pass2: EditText
    private lateinit var progress: ProgressBar
    private lateinit var mAuth: FirebaseAuth

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

        //ViewModel of this activity
        val SignupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        //go to login page
        buttonLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        //register user with email, password and username
        buttonSignUp.setOnClickListener{

            //validate whether all fields are filled up
            if(email.text.toString().isNotEmpty() && fullname.text.toString().isNotEmpty() && pass1.text.toString().isNotEmpty() && pass2.text.toString().isNotEmpty())
            {
                if(pass1.text.toString() != pass2.text.toString())
                    Toast.makeText(this,"Both passwords do not match", LENGTH_SHORT).show()
                else
                {
                    //show progress bar
                    progress.isVisible = true
                    //disable signup button
                    buttonSignUp.isEnabled = false

                    //call viewmodel function to create user on database
                    SignupViewModel.SignUp(email.text.toString(), pass1.text.toString(), fullname.text.toString())
                    SignupViewModel.returnVal.observe(this, Observer {
                        //if registration is successful, log user in and display main library
                        if(it == "success") {
                            val intent = Intent(this, Library::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            //hide progress bar
                            progress.isVisible = false
                            //enable signup button
                            buttonSignUp.isEnabled = true
                            //display error message
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