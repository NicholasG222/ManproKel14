package com.example.projectmanpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText


class EditGrup : AppCompatActivity() {
    companion object{
        const val dataTerima = "Data_Terima"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_grup)
//        val terimaDataGrup = intent.getParcelableArrayListExtra<Grup>(dataTerima)
//
//        var editTitleG = findViewById<EditText>(R.id.editTextTitleG)
//        var editCategoryG = findViewById<EditText>(R.id.editTextCategoryG)
//
//        var text = "${terimaDataGrup!![0].nama.toString()}"
//        editTitleG.text = text
       // editCategoryG.setText("${terimaDataGrup[0]?.kategori.toString()}")

    }
}

