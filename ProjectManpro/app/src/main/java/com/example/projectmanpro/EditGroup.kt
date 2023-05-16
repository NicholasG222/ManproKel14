package com.example.projectmanpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class EditGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group)
        val back = findViewById<Button>(R.id.buttonBack)
        var editKategori = findViewById<EditText>(R.id.editTextCategoryG)
        var editNama = findViewById<EditText>(R.id.editTextTitleG)
        var submit = findViewById<Button>(R.id.buttonAddGroup2)
        val intentGrup = intent.getParcelableExtra<Grup>(data)
        var db = FirebaseFirestore.getInstance()
        db.collection("tbGrup").document(intentGrup!!.nama!!).get().addOnSuccessListener { result ->
            editNama.setText(result.getString("nama"))
            editKategori.setText(result.getString("kategori"))
        }
        submit.setOnClickListener {
            var kategori = editKategori.text.toString()
            var nama = editNama.text.toString()
            var newGrup = Grup(nama, kategori)
            db.collection("tbGrup").document(intentGrup!!.nama!!).set(newGrup).addOnSuccessListener {
                Toast.makeText(
                    this@EditGroup, "Grup berhasil diubah",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val intent = Intent(this@EditGroup, MainAdmin::class.java)
            startActivity(intent)
        }
        back.setOnClickListener {
            val intent = Intent(this@EditGroup, MainAdmin::class.java)
            startActivity(intent)
        }
    }
    companion object{
        const val data = "data"
    }
}