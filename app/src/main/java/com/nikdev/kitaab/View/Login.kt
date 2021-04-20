package com.nikdev.kitaab.View

import android.content.Intent
import android.os.Bundle
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nikdev.kitaab.R
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var buttonRegister: TextView
    private lateinit var buttonLogin: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.login_progress)
        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser != null) {
            val intent = Intent(this, Library::class.java)
            startActivity(intent)
            finish()
        }
        else
            loginUser()
    }

    private fun loginUser()
    {
        buttonRegister = findViewById(R.id.go_to_register)
        buttonLogin = findViewById(R.id.login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        buttonRegister.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
            finish()
        }

        buttonLogin.setOnClickListener {
            progressBar.isVisible = true

            if(email.text.toString().isEmpty() && password.text.toString().isEmpty())
                Toast.makeText(this,"Please enter your credentials",LENGTH_SHORT).show()
            else if(email.text.toString().isEmpty())
                Toast.makeText(this,"Please enter your email address",LENGTH_SHORT).show()
            else if(password.text.toString().isEmpty())
                Toast.makeText(this,"Please enter your password",LENGTH_SHORT).show()
            else {
                mAuth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener { task->
                    if(task.isSuccessful) {
                        val intent = Intent(this, Library::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                        Toast.makeText(this,"Username or password is incorrect", LENGTH_SHORT).show()
                }
            }
        }
    }
}