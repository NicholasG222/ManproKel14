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
import com.google.firebase.firestore.FirebaseFirestore


class HomeUser : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    private var listPengumuman = arrayListOf<Pengumuman>()
    private var listGrup = arrayListOf<Grup>()
    private lateinit var rvPengumuman: RecyclerView
    private lateinit var rvGrup: RecyclerView
    private lateinit var _nama : MutableList<String>
    private lateinit var _kategori : MutableList<String>
    private var adapterG = AdapterGrup(listGrup)
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

                rvGrup.adapter =adapterG
            }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPengumuman = findViewById(R.id.rvPengumuman)
        rvGrup = findViewById(R.id.rvGrup)
        SiapkanData()
        adapterG.setOnItemClickCallback(object : AdapterGrup.OnItemClickCallback{


            override fun pindahEdit(data: Grup) {

                val eIntent = Intent(this@HomeUser, EditGrup::class.java)
                startActivity(eIntent)
            }

            override fun deleteGrup(pos: Grup) {

//                AlertDialog.Builder(this@HomeUser)
//                    .setTitle("HAPUS DATA")
//                    .setMessage("Apakah benar grup akan dihapus?")
//                    .setPositiveButton("HAPUS",
//                    DialogInterface.OnClickListener{ dialog, which ->
//
//                        _nama.removeAt(pos)
//                        _kategori.removeAt(pos)
//
//
//
//                    })
//
//                    .setNegativeButton("BATAL",
//                    DialogInterface.OnClickListener{ dialog, which ->
//
//                        Toast.makeText(this@HomeUser,
//                                        "DATA BATAL DIHAPUS",
//                                        Toast.LENGTH_SHORT).show()
//                    })
//                    .show()

//                db.collection("tbGrup").get().addOnSuccessListener {
//                        result ->
//                    for(document in result){
//
//                        val listGrup = document.get("nama") as MutableList<Any?>
//                        var bool = false
//                        var deleted: HashMap<String, Any>? = null
//
//                            for(i in listGrup){
//                                Log.d("debug", "tru")
//                                if((i as HashMap<String, Any>).get("nama") == data.nama){
//                                    bool = true
//                                    deleted = i
//
//                                }
//                            }
//                            if(bool){
//
//
//                                listGrup.remove(deleted)
//                            }
//
//                            db.collection("tbUser").document().update("nama", listGrup)
//                        db.collection("tbUser").document().update("kategori", listGrup)
//
//
//                    }
//                }

//                db.collection("tbGrup").document()
//                    .delete()
//                    .addOnSuccessListener { Log.d(TAG, "document deleted") }
//                    .addOnFailureListener{ Log.d(TAG,"gagal bang")}


            }

        }
        )

        var seeMore1 = findViewById<TextView>(R.id.textViewMore)
        var seeMore2 = findViewById<TextView>(R.id.textViewMore2)


        val buttonAddP = findViewById<Button>(R.id.buttonAddAnn)
        val buttonAddG = findViewById<Button>(R.id.buttonAddGroup)
        buttonAddP.setOnClickListener {

            val eIntent = Intent(this@HomeUser,AddPengumuman::class.java)
            startActivity(eIntent)
        }

        buttonAddG.setOnClickListener {

            val eIntent = Intent(this@HomeUser,AddGrup::class.java)
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