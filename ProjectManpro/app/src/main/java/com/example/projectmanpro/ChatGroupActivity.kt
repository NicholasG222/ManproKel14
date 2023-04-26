package com.example.projectmanpro

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanpro.R
import com.google.firebase.firestore.FirebaseFirestore

class ChatGroupActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    private var listPengumuman = arrayListOf<pengumumangrup>()
    private lateinit var _nama : Array<String>
    private lateinit var _detail : Array<String>
    private lateinit var _foto : TypedArray
    private lateinit var rvPengumumanGrup: RecyclerView
    private var arPengumuman = arrayListOf<pengumumangrup>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_group)

        rvPengumumanGrup = findViewById(R.id.rvChatGrup)
        SiapkanData()
    }

    private fun SiapkanData(){
        db.collection("tbPengumumanGrup")
            .get()
            .addOnSuccessListener { result ->
                val dataIntent = intent.getStringExtra(data)
                Log.d("data",dataIntent.toString())
                for (document in result) {

                        var pengumumangrup = pengumumangrup(
                            document.getString("pengirim"),
                            document.getString("judul"),
                            document.getString("isi"),
                            document.getString("grup")
                        )
                        listPengumuman.add(pengumumangrup)
                    Log.d("list", listPengumuman.toString())



                }
                rvPengumumanGrup.layoutManager = LinearLayoutManager(this)
                rvPengumumanGrup.adapter = adapterPengumumanGrup(listPengumuman)
            }
    }
    companion object{
        const val data = "data"
    }
}