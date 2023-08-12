package com.example.chatapplication.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivitySingupBinding
import com.example.chatapplication.model.User
import com.example.chatapplication.viewModel.AuthViewModel
import com.example.chatapplication.viewModel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SingUpActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDBRef : DatabaseReference
    lateinit var binding: ActivitySingupBinding
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        val factory = AuthViewModelFactory(this)
        viewModel = ViewModelProvider(this,factory).get(AuthViewModel::class.java)


        binding.btnSingUp.setOnClickListener {
            if(binding.txtUsername.text!!.isEmpty()|| binding.txtEmail.text!!.isEmpty() || binding.txtPassword.text!!.isEmpty()){
                Toast.makeText(this,"Wrong inputs!",Toast.LENGTH_SHORT).show()
            }else {
                val name = binding.txtUsername.text.toString()
                val email = binding.txtEmail.text.toString()
                val password = binding.txtPassword.text.toString()

                viewModel.signup(name, email, password)
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}