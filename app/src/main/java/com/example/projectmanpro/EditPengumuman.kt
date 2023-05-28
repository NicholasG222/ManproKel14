package com.example.projectmanpro

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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

class EditPengumuman : AppCompatActivity() {
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private lateinit var date: String
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var db: FirebaseFirestore
    private lateinit var img: ImageView
    private lateinit var editNama: EditText
    private lateinit var isi: String
    private lateinit var gambar: String
    private lateinit var nama: String
    private lateinit var currEmail: String
    private lateinit var editIsi: EditText
    private var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pengumuman)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        date = getCurrentDate()

        val buttonBack = findViewById<Button>(R.id.buttonBackEditAnnouncement)
        editNama = findViewById<EditText>(R.id.editTextTitleG)
        editIsi = findViewById(R.id.editTextCategoryG)
        var submit = findViewById<Button>(R.id.buttonAddGroup2)
        img = findViewById<ImageView>(R.id.imageView)
        val intentGrup = intent.getParcelableExtra<Pengumuman>(EditPengumuman.data)
        db = FirebaseFirestore.getInstance()
        var sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        currEmail = sp.getString("spRegister", null)!!
        db.collection("tbPengumuman").document(intentGrup!!.judul!!).get().addOnSuccessListener { result ->
            nama = result.getString("judul")!!
            editNama.setText(nama)
            isi = result.getString("isi")!!
            editIsi.setText(isi)
            gambar = result.getString("image")!!
            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(gambar)
            imageRef.getBytes(100*100*1024).addOnSuccessListener {
                val bitmap =  BitmapFactory.decodeByteArray(it, 0, it.size)
                img.setImageBitmap(bitmap)

            }
        }
        img.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Pilih gambar"),
                PICK_IMAGE_REQUEST
            )
        }
        submit.setOnClickListener {

            uploadImage()

        }

        buttonBack.setOnClickListener {
            val eIntent = Intent(this@EditPengumuman, MainAdmin::class.java)
            startActivity(eIntent)
        }
    }
    companion object{
        const val data = "data"
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
                img.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage() {

        if (filePath != null) {


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

                    Toast.makeText(this, "Gagal upload image", Toast.LENGTH_LONG).show()
                }
        } else {
            val ann =
                Pengumuman(gambar, editNama.text.toString(),"Edited on ${date}", editIsi.text.toString())

            ann.judul?.let { it1 ->
                db.collection("tbPengumuman").document(nama).set(ann)
                    .addOnSuccessListener {
                        editNama.setText("")
                        editIsi.setText("")



                        Toast.makeText(this, "Edit data berhasil", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@EditPengumuman, MainAdmin::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal melakukan edit data", Toast.LENGTH_LONG).show()
                    }
            }

        }
    }


    private fun addUploadRecordToDb(uri: String) {
        val ann =
            Pengumuman(uri, editNama.text.toString(),"Edited on ${date}", editIsi.text.toString())

        ann.judul?.let { it1 ->
            db.collection("tbPengumuman").document(nama).set(ann)
                .addOnSuccessListener {
                    editNama.setText("")
                    editIsi.setText("")



                    Toast.makeText(this, "Edit data berhasil", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@EditPengumuman, MainAdmin::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal melakukan edit data", Toast.LENGTH_LONG).show()
                }
        }
    }
}