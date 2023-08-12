package com.example.chatapplication.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.adapters.UserAdapter
import com.example.chatapplication.model.Authentication
import com.example.chatapplication.model.DATABASE
import com.example.chatapplication.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class AuthViewModel(val context: Context) : ViewModel() {

    var authentication: Authentication = Authentication(context)

    fun login(email:String,password:String){
        authentication.Login(email,password)
    }

    fun signup(name:String,email:String, password: String){
        authentication.singup(name,email,password)
    }

    fun logout(){
        authentication.signOut()
    }

}