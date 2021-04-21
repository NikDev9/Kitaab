package com.example.kitaab.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kitaab.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupViewModel : ViewModel() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var dataRef: DatabaseReference
    //to handle UI via the view
    var returnVal = MutableLiveData<String>()

    //to register user on the app
    fun SignUp(email: String, password: String, name: String) {
        //Firebase auth creates a new user on the database with just email and password
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //to get current user's Id
                val userId = mAuth.currentUser!!.uid
                dataRef = FirebaseDatabase.getInstance().reference.child("users")
                //store user info in the database using User data model
                dataRef.child(userId).setValue(User(name,email,"user images/default_pic.jpg")).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        this.returnVal.value = "success"
                    } else {
                        this.returnVal.value = task.exception.toString()
                    }
                }
            }
            else {
                val index = task.exception.toString().indexOf(':')
                if (index == -1)
                    this.returnVal.value = "Something went wrong. Please try again"
                else {
                    val substr = task.exception.toString().substring(index + 1)
                    this.returnVal.value = substr
                }
            }
        }
    }

}