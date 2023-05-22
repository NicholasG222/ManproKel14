package com.example.projectmanpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore


class AddGroup : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    private lateinit var inputTitle: EditText
    private lateinit var inputCategory: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        val buttonAdd = findViewById<Button>(R.id.buttonAddGroup2)
        inputTitle = findViewById<EditText>(R.id.editTextTitleG)
        inputCategory = findViewById<Spinner>(R.id.spinnerCategoryG)
        val dataGrup: ArrayList<Grup> = arrayListOf()


        var sp = getSharedPreferences("data_SP", MODE_PRIVATE)

        db = FirebaseFirestore.getInstance()
        var adapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, resources
                .getStringArray(R.array.group_category)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputCategory.adapter = adapter
        var kategori = "HIMA"
        inputCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 1) {
                    kategori = "Kelas"
                } else if (p2 == 2) {
                    kategori = "Tugas akhir"
                } else if (p2 == 3) {
                    kategori = "LEAP"
                } else if (p2 == 4) {
                    kategori = "Bimbingan TA"
                } else if (p2 == 5) {
                    kategori = "Laboratorium"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                kategori = "HIMA"
            }
        }
        buttonAdd.setOnClickListener {

            val group = Grup(inputTitle.text.toString(), kategori)

            db.collection("tbGrup").document(inputTitle.text.toString()).set(group)
            var roleList = ""
            var kat = group.kategori
            if (kat == "HIMA") {
                Log.d("test", "sukses")
                roleList = roleList.plus(" HIMA")
            } else if (kat == "Tugas akhir") {
                roleList =roleList.plus(" Koordinator skripsi")
            } else if (kat == "LEAP") {

                roleList =roleList.plus(" Kaprodi")
                roleList =roleList.plus(" Wakil kaprodi")
                roleList =roleList.plus(" Sekretaris")
                roleList =roleList.plus(" Koordinator skripsi")
            } else if (kat == "Kelas" || kat == "Laboratorium" || kat == "Bimbingan TA") {
                roleList = roleList.plus(" " + sp.getString("spRegister", null))
            }
            Log.d("test", roleList)
            var roleEditor = GrupEditorRole(kat, roleList)
            db.collection("tbEditorRole").document(group.kategori.toString()).set(roleEditor)
                .addOnSuccessListener {
                    Toast.makeText(this, "Role editor berhasil diatur", Toast.LENGTH_LONG).show()
                }
            inputTitle.setText("")


            Toast.makeText(this, "Simpan data berhasil", Toast.LENGTH_LONG).show()



            dataGrup.add(Grup(inputTitle.text.toString(), kategori))
            val eIntent = Intent(this@AddGroup, MainAdmin::class.java)
            startActivity(eIntent)
        }
    }

}