package com.example.chatapplication.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.databinding.ActivityLoginBinding
import com.example.chatapplication.viewModel.AuthViewModel
import com.example.chatapplication.viewModel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        mAuth = FirebaseAuth.getInstance()
        val factory = AuthViewModelFactory(this)
        viewModel = ViewModelProvider(this,factory).get(AuthViewModel::class.java)


        binding.btnLogin.setOnClickListener {
            if(binding.txtUsername.text!!.isEmpty() && binding.txtPassword.text!!.isEmpty()){
                Toast.makeText(this,"Please write a correct email and password",Toast.LENGTH_SHORT).show()
            }else{
            val email = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()
                  viewModel.login(email,password)
                 // Login(email,password)
            }
        }
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }
    }

}