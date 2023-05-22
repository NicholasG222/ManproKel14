package com.example.projectmanpro

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanpro.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ChatGroupActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    private var listPengumuman = arrayListOf<pengumumangrup>()
    private lateinit var _nama : Array<String>
    private lateinit var _detail : Array<String>
    private lateinit var _foto : TypedArray
    private lateinit var rvPengumumanGrup: RecyclerView
    private var arPengumuman = arrayListOf<pengumumangrup>()
    lateinit var dataIntent: String
    lateinit var sp: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_group)

        rvPengumumanGrup = findViewById(R.id.rvChatGrup)
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        var buttonSend = findViewById<FloatingActionButton>(R.id.fabSend)
        var inputChat = findViewById<EditText>(R.id.editChat)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        dataIntent = intent.getStringExtra(data)!!
        var dataRole = intent.getStringExtra(data2)!!
        buttonSend.isVisible = false
        inputChat.isVisible = false
        var currentRole = sp.getString("spRole", null)

        var currEmail = sp.getString("spRegister", null)
        if(dataRole == "Kelas" || dataRole == "Laboratorium" || dataRole == "Bimbingan TA"){
            db.collection("tbEditorRole").document(dataRole!!).get()
                .addOnSuccessListener { result ->
                    val editorRole = result.getString("editor")
                    Log.d("role3", editorRole.toString())
                    if( editorRole!!.contains(currEmail!!)){
                        buttonSend.isVisible = true
                        inputChat.isVisible = true


                    }
                }
        }else {
            db.collection("tbEditorRole").document(dataRole!!).get()
                .addOnSuccessListener { result ->
                    val editorRole = result.getString("editor")

                    if( editorRole!!.contains(currentRole!!)){
                        Log.d("role4", "true")
                        buttonSend.isVisible = true
                        inputChat.isVisible = true


                    }
                }
        }



        SiapkanData()
        buttonSend.setOnClickListener {
            var pgrup = pengumumangrup(currEmail, "Pengumuman ${dataIntent}", inputChat.text.toString(), dataIntent)
            db.collection("tbPengumumanGrup").document(pgrup.isi!!).set(pgrup).addOnSuccessListener {
                Toast.makeText(
                    this@ChatGroupActivity, "Pengumuman berhasil ditambahkan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        buttonBack.setOnClickListener {
            if(currentRole != "user") {
                val intent = Intent(this@ChatGroupActivity, MainAdmin::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@ChatGroupActivity, HomeUser::class.java)
                startActivity(intent)
            }
        }
    }

    private fun SiapkanData(){
        db.collection("tbPengumumanGrup")
            .get()
            .addOnSuccessListener { result ->


                for (document in result) {
                        if(document.getString("grup") == dataIntent) {
                            var pengumumangrup = pengumumangrup(
                                document.getString("pengirim"),
                                document.getString("judul"),
                                document.getString("isi"),
                                document.getString("grup")
                            )
                            listPengumuman.add(pengumumangrup)
                        }




                }
                rvPengumumanGrup.layoutManager = LinearLayoutManager(this)
                rvPengumumanGrup.adapter = adapterPengumumanGrup(listPengumuman)
            }
    }
    companion object{
        const val data = "data"
        const val data2 = "data2"
    }
}