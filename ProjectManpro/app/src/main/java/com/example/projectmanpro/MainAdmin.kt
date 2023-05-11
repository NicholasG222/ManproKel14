package com.example.projectmanpro

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class MainAdmin : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    private var listPengumuman = arrayListOf<Pengumuman>()
    private var listGrup = arrayListOf<Grup>()
    private lateinit var rvPengumuman: RecyclerView
    private lateinit var rvGrup: RecyclerView
    private lateinit var _nama : MutableList<String>
    private lateinit var _kategori : MutableList<String>
    private var adapterG = AdapterGrupAdmin(listGrup)
    //  val sp = getSharedPreferences("data_SP", MODE_PRIVATE)


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
                rvPengumuman.adapter = AdapterPengumumanAdmin(listPengumuman)
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

                rvGrup.adapter =adapterG
            }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)

        rvPengumuman = findViewById(R.id.rvPengumuman)
        rvGrup = findViewById(R.id.rvGrup)
        var listRequests = arrayListOf<AdminAccessRequests>()


        SiapkanData()

        adapterG.setOnItemClickCallback(object : AdapterGrupAdmin.OnItemClickCallback{


            override fun pindahEdit(data: Grup) {

                val eIntent = Intent(this@MainAdmin, EditGroup::class.java)
                startActivity(eIntent)
            }

            override fun deleteGrup(pos: Grup) {


            }


        }
        )

        var seeMore1 = findViewById<TextView>(R.id.textViewMore)
        var seeMore2 = findViewById<TextView>(R.id.textViewMore2)


        val buttonAddP = findViewById<Button>(R.id.buttonAddAnn)
        val buttonAddG = findViewById<Button>(R.id.buttonAddGroup)
        var fabAccept = findViewById<FloatingActionButton>(R.id.fabAccept)
        var listText = findViewById<TextView>(R.id.textViewAccess)
        fabAccept.setOnClickListener {
            db.collection("tbRequests")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        var requests = AdminAccessRequests(document.getString("email"), document.getString("role"))
                        listRequests.add(requests)

                    }
                    listText.setText(listRequests.toString())
//
                }


        }
        buttonAddP.setOnClickListener {

            val eIntent = Intent(this@MainAdmin,AddPengumuman::class.java)
            startActivity(eIntent)
        }

        buttonAddG.setOnClickListener {

            val eIntent = Intent(this@MainAdmin,AddGroup::class.java)
            startActivity(eIntent)

        }








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
                    rvPengumuman.adapter = AdapterPengumumanAdmin(listPengumuman)
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
                    rvGrup.adapter = AdapterGrupAdmin(listGrup)
                }
        }


    }
}