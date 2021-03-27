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
    var returnVal = MutableLiveData<String>()

    fun SignUp(email: String, password: String, name: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = mAuth.currentUser!!.uid
                dataRef = FirebaseDatabase.getInstance().reference.child("users")
                dataRef.child(userId).setValue(User(name,email,"user images/default_pic.jpg","")).addOnCompleteListener { task ->
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