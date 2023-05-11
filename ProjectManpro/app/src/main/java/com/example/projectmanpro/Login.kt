package com.example.projectmanpro

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

class Login : AppCompatActivity() {
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
        //if(currentUser != null){
           // val intent = Intent(applicationContext, HomeUser::class.java)
           // startActivity(intent)
           // finish()
      //  }
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
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
            finish()
        }

        buttonLogin.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val email: String = EditTextemail.text.toString()
            val password: String = EditTextpassword.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this@Login, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this@Login, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        if(email.contains("@peter.")) {
                            val intent = Intent(applicationContext, MainAdmin::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            val intent = Intent(applicationContext, HomeUser::class.java)
                            startActivity(intent)
                            finish()
                        }

                            Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT)
                                .show()
                            val eIntent = Intent(this@Login, HomeUser::class.java)
                            startActivity(eIntent)

                    } else {

                        Toast.makeText(this@Login, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }


        }
    }
}