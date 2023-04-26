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
import android.widget.SearchView
import android.widget.Toast
import com.example.projectmanpro.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList


class HomeUser : AppCompatActivity() {
    private lateinit var searchView: SearchView
    val db = FirebaseFirestore.getInstance();
    private var listPengumuman = arrayListOf<Pengumuman>()
    private var listGrup = arrayListOf<Grup>()
    private var filter = arrayListOf<Pengumuman>()
    private var filterGrup = arrayListOf<Grup>()
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
    private fun filterList(query : String?){

        if (query != null){
            val filteredList = arrayListOf<Pengumuman>()
            val filteredGrup = arrayListOf<Grup>()
            Log.d("list",filter.toString())
            for (i in filter){
                if (i.judul!!.lowercase().contains(query) || i.isi!!.lowercase().contains(query)){
                    filteredList.add(i)
                }
            }

            for (j in filterGrup){
                if (j.nama!!.lowercase().contains(query) || j.kategori!!.lowercase().contains(query)){
                    filteredGrup.add(j)
                }
            }
            var emptyPengumuman = arrayListOf<Pengumuman>()
            var emptyGrup = arrayListOf<Grup>()
            if (filteredList.isEmpty() && filteredGrup.isEmpty()){
                rvPengumuman.layoutManager = LinearLayoutManager(this)
                rvPengumuman.adapter = AdapterPengumuman(emptyPengumuman)
                rvGrup.layoutManager = LinearLayoutManager(this)
                rvGrup.adapter = AdapterGrup(emptyGrup)
            } else {
                rvPengumuman.layoutManager = LinearLayoutManager(this)
                rvPengumuman.adapter = AdapterPengumuman(filteredList)
                rvGrup.layoutManager = LinearLayoutManager(this)
                rvGrup.adapter = AdapterGrup(filteredGrup)

            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.search)
        rvPengumuman = findViewById(R.id.rvPengumuman)
        rvGrup = findViewById(R.id.rvGrup)
        var seeMore1 = findViewById<TextView>(R.id.textViewMore)
        var seeMore2 = findViewById<TextView>(R.id.textViewMore2)

        db.collection("tbPengumuman")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    var pengumuman = Pengumuman(document.getString("judul"), "general", document.getString("isi"))
                    filter.add(pengumuman)


                }
//                    rvPengumuman.layoutManager = LinearLayoutManager(this)
//                    rvPengumuman.adapter = AdapterPengumuman(listPengumuman)
            }

        db.collection("tbGrup")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    var grup = Grup(document.getString("nama"), document.getString("kategori"))
                    filterGrup.add(grup)


                }
//                    rvGrup.layoutManager = LinearLayoutManager(this)
//                    rvGrup.adapter = AdapterGrup(listGrup)
//                }
        SiapkanData()
//        seeMore1.setOnClickListener {
//            seeMore1.isVisible = false
//            listPengumuman = arrayListOf<Pengumuman>()
//            db.collection("tbPengumuman")
//                .get()
//                .addOnSuccessListener { result ->
//
//                    for (document in result) {
//                        var pengumuman = Pengumuman(document.getString("judul"), "general", document.getString("isi"))
//                        listPengumuman.add(pengumuman)
//
//
//                    }
//                    rvPengumuman.layoutManager = LinearLayoutManager(this)
//                    rvPengumuman.adapter = AdapterPengumuman(listPengumuman)
//                }
//        }
//
//        seeMore2.setOnClickListener {
//            seeMore2.isVisible = false
//            listGrup = arrayListOf<Grup>()
//            db.collection("tbGrup")
//                .get()
//                .addOnSuccessListener { result ->
//
//                    for (document in result) {
//                        var grup = Grup(document.getString("nama"), document.getString("kategori"))
//                        listGrup.add(grup)
//
//
//                    }
//                    rvGrup.layoutManager = LinearLayoutManager(this)
//                    rvGrup.adapter = AdapterGrup(listGrup)
//                }
//        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })


    }
}
}