package com.example.projectmanpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screem)
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, Login::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}