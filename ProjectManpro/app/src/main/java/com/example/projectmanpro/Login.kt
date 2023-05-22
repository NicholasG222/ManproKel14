package com.example.projectmanpro

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    private lateinit var EditTextemail: TextInputEditText
    private lateinit var EditTextpassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var auth: FirebaseAuth
    lateinit var sp: SharedPreferences
    private lateinit var progressBar : ProgressBar
    private lateinit var textView : TextView
    var db = FirebaseFirestore.getInstance()

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        val isiSP = sp.getString("spRegister", null)
        val isiRole = sp.getString("spRole", null)

        val user = User("email", "password", "role")
        if(currentUser != null && isiSP != null && isiRole != null){
            db.collection("tbUser").document(isiSP).get().addOnSuccessListener { result ->
                user.email = result.getString("email")
                user.password = result.getString("password")
                user.role = result.getString("role")
            }
            if( user.role != "user"){
                val intent = Intent(this@Login, MainAdmin::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@Login,HomeUser::class.java)
                startActivity(intent)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        EditTextemail = findViewById(R.id.email)
        EditTextpassword = findViewById(R.id.password)
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
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
                        val editor = sp.edit()

                        Toast.makeText(applicationContext, "Login berhasil", Toast.LENGTH_SHORT)
                            .show()
                        val user = User("email", "password", "role")
                        db.collection("tbUser").document(email).get().addOnSuccessListener { result ->
                            user.email = result.getString("email")
                            user.password = result.getString("password")
                            user.role = result.getString("role")
                            editor.putString("spRegister", email)
                            editor.putString("spRole", user.role)

                            editor.apply()
                            if(user.role != "user") {
                                val intent = Intent(applicationContext,MainAdmin::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                val intent = Intent(applicationContext, HomeUser::class.java)
                                startActivity(intent)
                                finish()
                            }
                       }






                    } else {

                        Toast.makeText(this@Login, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }


        }
    }
}