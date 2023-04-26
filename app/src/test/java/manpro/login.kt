package com.example.manpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    private lateinit var EditTextemail: TextInputEditText
    private lateinit var EditTextpassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar : ProgressBar
    private lateinit var textView : TextView

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        EditTextemail = findViewById(R.id.email)
        EditTextpassword = findViewById(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.registerNow)
        buttonLogin = findViewById(R.id.btn_login)
        textView.setOnClickListener {
            val intent = Intent(applicationContext, register::class.java)
            startActivity(intent)
            finish()
        }

        buttonLogin.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val email: String = EditTextemail.text.toString()
            val password: String = EditTextpassword.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this@login, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this@login, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(this@login, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }
}