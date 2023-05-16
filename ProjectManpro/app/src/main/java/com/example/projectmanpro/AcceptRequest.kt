package com.example.projectmanpro

import android.content.Intent
import android.net.MailTo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AcceptRequest : AppCompatActivity() {
    private var arrayRequest = arrayListOf<AdminAccessRequests>()
    val db = FirebaseFirestore.getInstance()
    private lateinit var rvRequests: RecyclerView
    private fun SiapkanData(){
        db.collection("tbRequests")
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    var request = AdminAccessRequests(document.getString("email"), document.getString("role"), document.getString("catatan"))
                    arrayRequest.add(request)
                }

                val adapterR = adapterRequest(arrayRequest)
                rvRequests.layoutManager = LinearLayoutManager(this)
                rvRequests.adapter = adapterR
                adapterR.setOnItemClickCallback(object: adapterRequest.OnItemClickCallback{
                    override fun accept(data: AdminAccessRequests) {
                        db.collection("tbUser").document(data.email!!).update("role", data.role).addOnSuccessListener {
                            Toast.makeText(this@AcceptRequest, "Role berhasil diubah",
                                Toast.LENGTH_SHORT).show()
                        }

                        db.collection("tbRequests").document(data.email!!).delete()
                        arrayRequest.remove(data)
                        SiapkanData()
                    }

                    override fun reject(data: AdminAccessRequests) {
                        Toast.makeText(this@AcceptRequest, "Request ditolak",
                            Toast.LENGTH_SHORT).show()
                        db.collection("tbRequests").document(data.email!!).delete()

                        arrayRequest.remove(data)
                        SiapkanData()
                    }

                })

                }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_request)
        rvRequests = findViewById(R.id.rvRequest)
        var backButton = findViewById<Button>(R.id.buttonBackAR)
        SiapkanData()
        backButton.setOnClickListener {
            val intent = Intent(this@AcceptRequest, MainAdmin::class.java)
            startActivity(intent)
        }

    }
}