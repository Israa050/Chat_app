package com.example.chatapplication.model

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.System.getString
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.example.chatapplication.R
import com.example.chatapplication.view.LoginActivity
import com.example.chatapplication.view.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

const val DATABASE = "https://chatapplication-e6a6a-default-rtdb.europe-west1.firebasedatabase.app/"

class Authentication {

    private lateinit var context: Context
    private lateinit var firebaseUserMutableLiveData: MutableLiveData<FirebaseUser>
    private lateinit var userLoggedMutableLiveData: MutableLiveData<Boolean>
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDBRef : DatabaseReference

    constructor(context: Context){
        this.context = context
        this.firebaseUserMutableLiveData = MutableLiveData()
        this.userLoggedMutableLiveData = MutableLiveData()
        this.mAuth = FirebaseAuth.getInstance()
        this.mDBRef = FirebaseDatabase.getInstance(DATABASE).reference


        if(mAuth.currentUser != null){
            firebaseUserMutableLiveData.postValue(mAuth.currentUser)
        }
    }

    fun getUserLoggedMutableLiveData():MutableLiveData<Boolean>{
        return userLoggedMutableLiveData
    }

    fun getFirebaseUserMutableLiveData(): MutableLiveData<FirebaseUser>{
        return firebaseUserMutableLiveData
    }

    fun Login(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val intent = Intent(context, MainActivity::class.java)
                startActivity(context,intent,Bundle())
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(context,"User does not exist", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun singup(name :String,email: String,password: String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                addUserToDatabase(name,email,mAuth.currentUser!!.uid)
                val intent = Intent(context, MainActivity::class.java)
                 startActivity(context,intent,Bundle())
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(context,"An error occurred", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        val user = User(name,email,uid)
        mDBRef.child("Users").child(uid).setValue(user)
    }

    fun signOut(){
        mAuth.signOut()
        userLoggedMutableLiveData.postValue(true)
    }



}
