package com.example.chatapplication.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.adapters.MessageAdapter
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityChatBinding
import com.example.chatapplication.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding
    lateinit var mAuth : FirebaseAuth
    lateinit var mDBRef : DatabaseReference
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<Message>

    var senderRoom : String? = null
    var receiverRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDBRef = FirebaseDatabase.getInstance(getString(R.string.Database)).reference
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid


        supportActionBar?.title = name
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor(getString(R.string.actionBar_color))))


        initializer()
        storeMessagesIndatabase()

        binding.sendButton.setOnClickListener {
            val message = binding.messageBox.text.toString()
            val messageObject = Message(message,senderUid)

            mDBRef.child("Chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDBRef.child("Chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binding.messageBox.setText("")
        }

    }

    private fun storeMessagesIndatabase() {
        mDBRef.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    private fun initializer() {
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)
        binding.recyclerviewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewChat.adapter = messageAdapter
    }
}