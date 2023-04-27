package com.example.projectmanpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var button: Button
    private lateinit var textView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        auth  = FirebaseAuth.getInstance()
        button = findViewById(R.id.logout)
        textView = findViewById(R.id.user_details)
        user = auth.currentUser!!
        //val currentUser = user
        if (user == null){
            val intent = Intent(applicationContext, login::class.java)
            startActivity(intent)
            finish()
        }else{
            textView.setText(user.email)
        }
        button.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, login::class.java)
            startActivity(intent)
            finish()
        }
    }
}