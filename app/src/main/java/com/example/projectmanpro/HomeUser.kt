package com.example.projectmanpro

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text

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
    private lateinit var textKosong: TextView
    private lateinit var textKosongGrup: TextView
    lateinit var sp: SharedPreferences
    private var adapterG = AdapterGrup(listGrup)

    private fun SiapkanData(){

        db.collection("tbPengumuman")
            .get()
            .addOnSuccessListener { result ->
                var count = 0
                for (document in result) {
                    var pengumuman = Pengumuman(document.getString("image"),document.getString("judul"), document.getString("date"), document.getString("isi"))
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
                    var grup = Grup(document.getString("gambar"),document.getString("nama"), document.getString("kategori"),
                        document.getString("createdBy"))
                    listGrup.add(grup)
                    count++
                    if(count > 1){
                        break
                    }

                }
                rvGrup.layoutManager = LinearLayoutManager(this)
                val adapterG = AdapterGrup(listGrup)
                rvGrup.adapter = adapterG
                setCallbackG()
                adapterG.setOnItemClickCallback(object: AdapterGrup.OnItemClickCallback{
                    override fun imageClicked(data: Grup) {
                        val eIntent = Intent(this@HomeUser, ChatGroupActivity::class.java).apply{
                            putExtra(ChatGroupActivity.data, data.nama.toString())
                            putExtra(ChatGroupActivity.data2, data.kategori.toString())
                        }
                        startActivity(eIntent)
                    }

                })
            }


    }
    private fun filterList(query : String?){
        textKosong = findViewById(R.id.textViewKosong)
        textKosongGrup = findViewById(R.id.textViewKosongGrup)


        if (query != null){
            val filteredList = arrayListOf<Pengumuman>()
            val filteredGrup = arrayListOf<Grup>()

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
                if(filteredList.isEmpty()) {
                    textKosong.isVisible = true
                }
                if(filteredGrup.isEmpty()) {
                    textKosongGrup.isVisible = true
                }
            } else {
                rvPengumuman.layoutManager = LinearLayoutManager(this)
                rvPengumuman.adapter = AdapterPengumuman(filteredList)
                rvGrup.layoutManager = LinearLayoutManager(this)
                rvGrup.adapter = AdapterGrup(filteredGrup)
                textKosong.isVisible = false
                textKosongGrup.isVisible = false
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textKosong = findViewById(R.id.textViewKosong)
        textKosongGrup = findViewById(R.id.textViewKosongGrup)

        searchView = findViewById(R.id.search)
        rvPengumuman = findViewById(R.id.rvPengumuman)
        var textEmail = findViewById<TextView>(R.id.textViewEmail)
        rvGrup = findViewById(R.id.rvGrup)
        textKosong.isVisible = false
        textKosongGrup.isVisible = false
        var seeMore1 = findViewById<TextView>(R.id.textViewMore)
        var seeMore2 = findViewById<TextView>(R.id.textViewMore2)
//        var fabReq = findViewById<FloatingActionButton>(R.id.fabAccess)
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        var email = sp.getString("spRegister", null)
        textEmail.setText("Log in sebagai: ${email}")


        db.collection("tbPengumuman")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    var pengumuman = Pengumuman(
                        document.getString("image"),
                        document.getString("judul"),
                        document.getString("date"),
                        document.getString("isi")
                    )
                    filter.add(pengumuman)


                }

            }

        db.collection("tbGrup")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    var grup = Grup(
                        document.getString("gambar"),
                        document.getString("nama"),
                        document.getString("kategori"),
                        document.getString("createdBy")
                    )
                    filterGrup.add(grup)


                }

                SiapkanData()
                val isiSP = sp.getString("spRegister", null)

//                fabReq.setOnClickListener {
//                    val intent = Intent(this@HomeUser, AddRequest::class.java)
//                    startActivity(intent)
//
//                }

                seeMore1.setOnClickListener {
                    seeMore1.isVisible = false
                    listPengumuman = arrayListOf<Pengumuman>()
                    db.collection("tbPengumuman")
                        .get()
                        .addOnSuccessListener { result ->

                            for (document in result) {
                                var pengumuman = Pengumuman(
                                    document.getString("image"),
                                    document.getString("judul"),
                                    document.getString("date"),
                                    document.getString("isi")
                                )
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
                                var grup = Grup(
                                    document.getString("gambar"),
                                    document.getString("nama"),
                                    document.getString("kategori"),
                                    document.getString("createdBy")
                                )
                                listGrup.add(grup)


                            }
                            rvGrup.layoutManager = LinearLayoutManager(this)

                            rvGrup.adapter = adapterG
                            setCallbackG()

                        }
                }

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(newText: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterList(newText)
                        seeMore1.isVisible = false
                        seeMore2.isVisible = false
                        if (newText != null) {
                            if (newText.isEmpty()) {
                                seeMore1.isVisible = true
                                seeMore2.isVisible = true
                            }
                        }
                        return true
                    }
                })


            }
    }
        private fun setCallbackG(){
            adapterG.setOnItemClickCallback(object: AdapterGrup.OnItemClickCallback{
                override fun imageClicked(data: Grup) {
                    val eIntent = Intent(this@HomeUser, ChatGroupActivity::class.java).apply{
                        putExtra(ChatGroupActivity.data, data.nama.toString())
                    }

                    startActivity(eIntent)
                }

            })
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logout -> {
                val editor = sp.edit()
                editor.putString("spRegister", null)
                editor.putString("spRole", null)
                editor.apply()
                val intent = Intent(this@HomeUser, Login::class.java)
                startActivity(intent)
                return true
            }

            R.id.fabAccessMenu -> {
                val intent = Intent(this@HomeUser, AddRequest::class.java)
                startActivity(intent)
                return true
            }

            else -> {return false}
        }


        return super.onOptionsItemSelected(item)
    }


}