package com.example.chatapplication.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.R
import com.example.chatapplication.adapters.UserAdapter
import com.example.chatapplication.databinding.ActivityMainBinding
import com.example.chatapplication.model.User
import com.example.chatapplication.viewModel.AuthViewModel
import com.example.chatapplication.viewModel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var binding: ActivityMainBinding
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var mDBRef : DatabaseReference
    private lateinit var authViewModel : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar!!.title = "Messages"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor(getString(R.string.actionBar))))

        initializer()
        getUserData()

        userAdapter = UserAdapter(this@MainActivity, userList)
        binding.recyclerviewUser.adapter = userAdapter

    }

    private fun initializer() {
        binding.recyclerviewUser.layoutManager = LinearLayoutManager(this@MainActivity)
        mAuth = FirebaseAuth.getInstance()
        mDBRef = FirebaseDatabase.getInstance(getString(R.string.Database)).reference
        userList = ArrayList()
        val factory = AuthViewModelFactory(this)
        authViewModel = ViewModelProvider(this,factory)[AuthViewModel::class.java]

    }

    private fun getUserData() {

        mDBRef.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if(mAuth.currentUser?.uid != user?.uid){
                            userList.add(user!!)
                        }
                    }
                    userAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            authViewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
        return true
    }


}

