package com.example.projectmanpro

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class Register : AppCompatActivity() {

    private lateinit var EditTextemail: TextInputEditText
    private lateinit var EditTextpassword: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar : ProgressBar
    private lateinit var textView : TextView
    private var db = FirebaseFirestore.getInstance()
    lateinit var sp: SharedPreferences

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        val isiSP = sp.getString("spRegister", null)
        val user = User("email", "password", "role")
        if(currentUser != null && isiSP != null){
           db.collection("tbUser").document(isiSP).get().addOnSuccessListener { result ->
               user.email = result.getString("email")
               user.password = result.getString("password")
               user.role = result.getString("role")
           }
            if(user.role == "user" && ! user.email!!.contains("admin@peter.")){
                val intent = Intent(this@Register, HomeUser::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@Register, MainAdmin::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        EditTextemail = findViewById(R.id.email)
        EditTextpassword = findViewById(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.loginNow)
        buttonReg = findViewById(R.id.btn_register)


        textView.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
            finish()
        }


        buttonReg.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email: String = EditTextemail.text.toString()
            val password: String = EditTextpassword.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this@Register, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this@Register, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE

                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@Register, "Account Created.",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (task.isSuccessful) {
                            val editor = sp.edit()
                            editor.putString("spRegister", email)
                            editor.apply()

                            val user = User(email, password, "user")
                            user.email?.let { it1 ->
                                db.collection("tbUser").document(email.toString()).set(user)

                                if (email.contains("admin@peter.")) {
                                    val intent = Intent(applicationContext, MainAdmin::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    val intent = Intent(applicationContext, HomeUser::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                        } else {
                            //may be kalo salah in ganti kembali jadi baseContext
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@Register, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }

}