package com.example.projectmanpro

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class MainAdmin : AppCompatActivity() {
    private lateinit var searchView: SearchView
    val db = FirebaseFirestore.getInstance();
    private var listPengumuman = arrayListOf<Pengumuman>()
    private var listGrup = arrayListOf<Grup>()
    private lateinit var rvPengumuman: RecyclerView
    private lateinit var rvGrup: RecyclerView
    private lateinit var _nama : MutableList<String>
    private lateinit var _kategori : MutableList<String>
    private var filter = arrayListOf<Pengumuman>()
    private var filterGrup = arrayListOf<Grup>()
    private lateinit var textKosong: TextView
    private lateinit var textKosongGrup: TextView
    private lateinit var role: String
    private var bool = false
    private var adapterG = AdapterGrupAdmin(listGrup, bool)
    private var adapterA = AdapterPengumumanAdmin(listPengumuman, bool)
    lateinit var sp: SharedPreferences



    private fun SiapkanData(){
        db.collection("tbPengumuman")
            .get()
            .addOnSuccessListener { result ->
                var count = 0
                for (document in result) {
                    var pengumuman = Pengumuman(document.getString("image"),document.getString("judul"), document.getString("date"), document.getString("isi"), document.getString("original"))
                    listPengumuman.add(pengumuman)
                    count++
                    if(count > 2){
                        break
                    }

                }
                rvPengumuman.layoutManager = LinearLayoutManager(this)
                rvPengumuman.adapter = adapterA
                setCallbackA()
            }

        db.collection("tbGrup")
            .get()
            .addOnSuccessListener { result ->
                var count = 0
                for (document in result) {
                    var grup = Grup(document.getString("gambar"),document.getString("nama"), document.getString("kategori"), document.getString("createdBy"), document.getString("original"))
                    listGrup.add(grup)
                    count++
                    if(count > 2){
                        break
                    }

                }
                rvGrup.layoutManager = LinearLayoutManager(this)

                rvGrup.adapter =adapterG
                setCallbackG()
            }


    }
    private fun filterList(query : String?){
        textKosong = findViewById(R.id.textViewKosongAdmin)
        textKosongGrup = findViewById(R.id.textViewKosongGrupAdmin)


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
                rvPengumuman.adapter = adapterA
                setCallbackA()
                rvGrup.layoutManager = LinearLayoutManager(this)
                rvGrup.adapter = adapterG
                setCallbackG()
                textKosong.isVisible = false
                textKosongGrup.isVisible = false
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_admin)

        textKosong = findViewById(R.id.textViewKosongAdmin)
        textKosongGrup = findViewById(R.id.textViewKosongGrupAdmin)
        searchView = findViewById(R.id.searchButton)
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        var textEmail = findViewById<TextView>(R.id.textViewEmail)
        rvPengumuman = findViewById(R.id.rvPengumuman)
        rvGrup = findViewById(R.id.rvGrup)
        textKosong.isVisible = false
        textKosongGrup.isVisible = false



        SiapkanData()
        role = sp.getString("spRole", null)!!
        setCallbackG()
        setCallbackA()

        var seeMore1 = findViewById<TextView>(R.id.textViewMore)
        var seeMore2 = findViewById<TextView>(R.id.textViewMore2)
        var email = sp.getString("spRegister", null)
        textEmail.setText("Log in sebagai: ${email}")



        val buttonAddP = findViewById<Button>(R.id.buttonAddAnn)
        val buttonAddG = findViewById<Button>(R.id.buttonAddGroup)
        var fabAccept = findViewById<FloatingActionButton>(R.id.fabAccept)

        var arrayRole = arrayListOf<String>()
        bool = role == "Super Admin" || role == "Kaprodi" || role == "Wakil kaprodi" || role == "Sekretaris" ||role == "Koordinator skripsi"
        adapterG = AdapterGrupAdmin(listGrup, bool)
        adapterA = AdapterPengumumanAdmin(listPengumuman, bool)
        db.collection("tbDoubleRole").get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    var email = doc.getString("email")
                    var role = doc.getString("nama")
                    if (email == email) {
                        arrayRole.add(role!!)

                    }

                }
               if(arrayRole.contains("Super Admin") || arrayRole.contains("Kaprodi") || arrayRole.contains("Sekretaris") ||
                   arrayRole.contains("Wakil kaprodi") || arrayRole.contains("Koordinator skripsi")){
                       buttonAddP.isVisible = true
               }
            }
       if(role == "Super Admin") {
           fabAccept.setOnClickListener {
               val intent = Intent(this@MainAdmin, AcceptRequest::class.java)
               startActivity(intent)
           }
       }else{
           fabAccept.setOnClickListener {
               val intent = Intent(this@MainAdmin, AddRequest::class.java)
               startActivity(intent)
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

        db.collection("tbPengumuman")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    var pengumuman = Pengumuman(document.getString("image"), document.getString("judul"), document.getString("date"), document.getString("isi"),document.getString("original"))
                    filter.add(pengumuman)


                }

            }

        db.collection("tbGrup")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    var grup = Grup(
                        document.getString("image"),
                        document.getString("nama"),
                        document.getString("kategori"),
                        document.getString("createdBy"),
                        document.getString("original")

                    )
                    filterGrup.add(grup)


                }


                val isiSP = sp.getString("spRegister", null)
            }


        seeMore1.setOnClickListener {
            seeMore1.isVisible = false
            listPengumuman = arrayListOf<Pengumuman>()
            db.collection("tbPengumuman")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        var pengumuman = Pengumuman(document.getString("image"),document.getString("judul"), "general", document.getString("isi"), document.getString("original"))
                        listPengumuman.add(pengumuman)


                    }
                    rvPengumuman.layoutManager = LinearLayoutManager(this)
                    rvPengumuman.adapter = adapterA
                    setCallbackA()
                }
        }

        seeMore2.setOnClickListener {
            seeMore2.isVisible = false
            listGrup = arrayListOf<Grup>()
            db.collection("tbGrup")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        var grup = Grup(document.getString("gambar"),document.getString("nama"), document.getString("kategori"), document.getString("createdBy"), document.getString("original"))
                        listGrup.add(grup)


                    }
                    rvGrup.layoutManager = LinearLayoutManager(this)
                    rvGrup.adapter = adapterG
                    setCallbackG()
                }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                seeMore1.isVisible = false
                seeMore2.isVisible = false
                if (newText != null) {
                    if(newText.isEmpty()){
                        seeMore1.isVisible = true
                        seeMore2.isVisible = true
                    }
                }
                return true
            }
        })


    }


    fun setCallbackG(){
        bool = role == "Super Admin" || role == "Kaprodi" || role == "Wakil kaprodi" || role == "Sekretaris" ||role == "Koordinator skripsi"
        adapterG = AdapterGrupAdmin(listGrup, bool)
        adapterG.setOnItemClickCallback(object : AdapterGrupAdmin.OnItemClickCallback{


            override fun pindahEdit(data: Grup) {

                val eIntent = Intent(this@MainAdmin, EditGroup::class.java).apply{
                    putExtra(EditGroup.data, data)
                }
                startActivity(eIntent)
            }

            override fun deleteGrup(pos: Grup) {
                AlertDialog.Builder(this@MainAdmin).setTitle("Hapus grup")
                    .setMessage("Apakah Anda ingin menghapus grup?")
                    .setPositiveButton(
                        "HAPUS",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            db.collection("tbGrup").document(pos.original!!).delete()
                            listGrup.remove(pos)
                            adapterG.notifyDataSetChanged()


                        }).setNegativeButton(
                        "BATAL",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            Toast.makeText(
                                this@MainAdmin,
                                "BATAL",
                                Toast.LENGTH_SHORT
                            ).show()

                        }).show()

            }

            override fun imageClicked(data: Grup) {
                val eIntent = Intent(this@MainAdmin, ChatGroupActivity::class.java).apply{
                    putExtra(ChatGroupActivity.data, data.original.toString())
                    putExtra(ChatGroupActivity.data2, data.kategori.toString())
                }

                startActivity(eIntent)

            }


        }
        )
    }

    fun setCallbackA() {
        bool = role == "Super Admin" || role == "Kaprodi" || role == "Wakil kaprodi" || role == "Sekretaris" ||role == "Koordinator skripsi"
        adapterA = AdapterPengumumanAdmin(listPengumuman, bool)
        adapterA.setOnItemClickCallback(object :
            AdapterPengumumanAdmin.OnItemClickCallback {
            override fun onItemClicked(data: Pengumuman) {

            }

            override fun editAnn(data: Pengumuman) {
                val eIntent = Intent(this@MainAdmin, EditPengumuman::class.java).apply {
                    putExtra(EditPengumuman.data, data)
                }
                startActivity(eIntent)
            }

            override fun delAnn(data: Pengumuman) {
                AlertDialog.Builder(this@MainAdmin).setTitle("Hapus pengumuman")
                    .setMessage("Apakah Anda ingin menghapus pengumuman?")
                    .setPositiveButton(
                        "HAPUS",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            db.collection("tbPengumuman").document(data.original!!).delete()
                            listPengumuman.remove(data)
                            adapterA.notifyDataSetChanged()


                        }).setNegativeButton(
                        "BATAL",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            Toast.makeText(
                                this@MainAdmin,
                                "BATAL",
                                Toast.LENGTH_SHORT
                            ).show()

                        }).show()
            }


        }
        )
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logout -> {
                val editor = sp.edit()
                editor.clear()
                editor.apply()
                val intent = Intent(this@MainAdmin, Login::class.java)
                startActivity(intent)
                return true
            }
            else -> {return false}
        }


        return super.onOptionsItemSelected(item)
    }



}