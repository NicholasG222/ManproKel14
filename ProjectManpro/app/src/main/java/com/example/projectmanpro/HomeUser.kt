package com.example.projectmanpro

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class HomeUser : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    private var listPengumuman = arrayListOf<Pengumuman>()
    private var listGrup = arrayListOf<Grup>()
    private lateinit var rvPengumuman: RecyclerView
    private lateinit var rvGrup: RecyclerView

    private fun SiapkanData(){
        db.collection("tbPengumuman")
            .get()
            .addOnSuccessListener { result ->
                var count = 0
                for (document in result) {
                    var pengumuman = Pengumuman(document.getString("judul"), "general", document.getString("isi"))
                    listPengumuman.add(pengumuman)
                    count++
                    if(count > 1){
                        break
                    }

                }
                rvPengumuman.layoutManager = LinearLayoutManager(this)
                rvPengumuman.adapter = AdapterPengumuman(listPengumuman)
            }

        db.collection("tbGrup")
            .get()
            .addOnSuccessListener { result ->
                var count = 0
                for (document in result) {
                    var grup = Grup(document.getString("nama"), document.getString("kategori"))
                   listGrup.add(grup)
                    count++
                    if(count > 1){
                        break
                    }

                }
                rvGrup.layoutManager = LinearLayoutManager(this)
                rvGrup.adapter = AdapterGrup(listGrup)
            }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPengumuman = findViewById(R.id.rvPengumuman)
        rvGrup = findViewById(R.id.rvGrup)
        var seeMore1 = findViewById<TextView>(R.id.textViewMore)
        var seeMore2 = findViewById<TextView>(R.id.textViewMore2)
        SiapkanData()
        seeMore1.setOnClickListener {
            seeMore1.isVisible = false
            listPengumuman = arrayListOf<Pengumuman>()
            db.collection("tbPengumuman")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        var pengumuman = Pengumuman(document.getString("judul"), "general", document.getString("isi"))
                        listPengumuman.add(pengumuman)


                    }
                    rvPengumuman.layoutManager = LinearLayoutManager(this)
                    rvPengumuman.adapter = AdapterPengumuman(listPengumuman)
                }
        }

        seeMore2.setOnClickListener {
            seeMore2.isVisible = false
            listGrup = arrayListOf<Grup>()
            db.collection("tbGrup")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        var grup = Grup(document.getString("nama"), document.getString("kategori"))
                        listGrup.add(grup)


                    }
                    rvGrup.layoutManager = LinearLayoutManager(this)
                    rvGrup.adapter = AdapterGrup(listGrup)
                }
        }


    }
}