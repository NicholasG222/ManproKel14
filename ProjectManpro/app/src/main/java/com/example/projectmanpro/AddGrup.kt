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

class AddGrup : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var inputTitle: EditText
    private lateinit var inputCategory: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_grup)

        val buttonAdd = findViewById<Button>(R.id.buttonAddGroup2)
        inputTitle = findViewById<EditText>(R.id.editTextTitleG)
        inputCategory = findViewById<EditText>(R.id.editTextCategoryG)
        val dataGrup : ArrayList<Grup> = arrayListOf()


        var sp = getSharedPreferences("data_SP", MODE_PRIVATE)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        db = Firebase.firestore

        buttonAdd.setOnClickListener {
            val group = Grup(inputTitle.text.toString(),inputCategory.text.toString())
            group.nama?.let { it1 -> db.collection("tbGrup").document(inputTitle.text.toString()).set(group)
                .addOnSuccessListener {
                    inputTitle.setText("")
                    inputCategory.setText("")

                    Toast.makeText(this,"Simpan data berhasil", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Gagal menyimpan data", Toast.LENGTH_LONG).show()
                }
            }

            dataGrup.add(Grup(inputTitle.text.toString(),inputCategory.text.toString()))
            val eIntent = Intent(this@AddGrup, HomeUser::class.java).apply {
                putExtra(EditGrup.dataTerima, dataGrup)
            }
            startActivity(eIntent)
        }




    }
}