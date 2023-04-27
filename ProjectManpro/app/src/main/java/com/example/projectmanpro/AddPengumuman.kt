package com.example.projectmanpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AddPengumuman : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var inputTitle:EditText
    private lateinit var inputContent:EditText
    private lateinit var inputGeneral:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pengumuman)

        val buttonTambah = findViewById<Button>(R.id.buttonAddAnnounce2)
        inputTitle = findViewById<EditText>(R.id.editTextTitle)
        inputContent = findViewById<EditText>(R.id.editTextContent)
        inputGeneral = findViewById<EditText>(R.id.editTextGen)



        var sp = getSharedPreferences("data_SP", MODE_PRIVATE)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
//        val _inputTitle = inputTitle.text.toString()
//        val _inputContent = inputContent.text.toString()
//        val _inputGeneral = inputGeneral.text.toString()
        db = Firebase.firestore


        buttonTambah.setOnClickListener {
            val announcement = Pengumuman(inputTitle.text.toString(),inputGeneral.text.toString(), inputContent.text.toString())
            announcement.judul?.let { it1 -> db.collection("tbPengumuman").document(inputTitle.text.toString()).set(announcement)
                .addOnSuccessListener {
                    inputTitle.setText("")
                    inputGeneral.setText("")
                    inputContent.setText("")

                    Toast.makeText(this,"Simpan data berhasil", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Gagal menyimpan data", Toast.LENGTH_LONG).show()
                }
            }

            val eIntent = Intent(this@AddPengumuman, HomeUser::class.java)
            startActivity(eIntent)

        }

    }
}