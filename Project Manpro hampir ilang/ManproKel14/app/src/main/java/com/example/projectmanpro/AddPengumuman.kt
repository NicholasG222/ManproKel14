package com.example.projectmanpro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.projectmanpro.DateHelper.getCurrentDate
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*


class AddPengumuman : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    private lateinit var inputTitle: EditText
    private lateinit var inputContent: EditText
    private lateinit var imageView: ImageView

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private lateinit var date: String
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pengumuman)
        date = getCurrentDate()
        val buttonTambah = findViewById<Button>(R.id.buttonAddAnnounce2)
        inputTitle = findViewById<EditText>(R.id.editTextTitle)
        inputContent = findViewById<EditText>(R.id.editTextContent)
        imageView = findViewById(R.id.imageView)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        var sp = getSharedPreferences("data_SP", MODE_PRIVATE)

        db = FirebaseFirestore.getInstance()

        imageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Pilih gambar"),
                PICK_IMAGE_REQUEST
            )
        }
        buttonTambah.setOnClickListener {

            uploadImage()
            val eIntent = Intent(this@AddPengumuman, MainAdmin::class.java)
            startActivity(eIntent)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            Log.e("path", filePath.toString())
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask =
                uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        addUploadRecordToDb(downloadUri.toString())
                    }
                }?.addOnFailureListener {
                    Log.e("it", it.toString())
                    Toast.makeText(this, "Gagal upload image", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Image tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addUploadRecordToDb(uri: String) {
        val announcement =
            Pengumuman(uri, inputTitle.text.toString(), date, inputContent.text.toString(),inputTitle.text.toString())
        announcement.judul?.let { it1 ->
            db.collection("tbPengumuman").document(inputTitle.text.toString()).set(announcement)
                .addOnSuccessListener {
                    inputTitle.setText("")

                    inputContent.setText("")

                    Toast.makeText(this, "Simpan data berhasil", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_LONG).show()
                }
        }


    }
}