package com.example.projectmanpro

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*

class EditGroup : AppCompatActivity() {

    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private lateinit var date: String
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var db: FirebaseFirestore
    private lateinit var img: ImageView
    private lateinit var editNama: EditText
    private lateinit var kategori: String
    private lateinit var gambar: String
    private lateinit var nama: String
    private lateinit var currEmail: String

    private var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group)
        val back = findViewById<Button>(R.id.buttonBack)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        editNama = findViewById<EditText>(R.id.editTextTitleG)
        var submit = findViewById<Button>(R.id.buttonAddGroup2)
        img = findViewById<ImageView>(R.id.imageView)
        val intentGrup = intent.getParcelableExtra<Grup>(data)
        db = FirebaseFirestore.getInstance()
        var sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        currEmail = sp.getString("spRegister", null)!!

        db.collection("tbGrup").document(intentGrup!!.nama!!).get().addOnSuccessListener { result ->
            nama = result.getString("nama")!!
            editNama.setText(nama)
            kategori = result.getString("kategori")!!
            gambar = result.getString("gambar")!!
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
        back.setOnClickListener {
            val intent = Intent(this@EditGroup, MainAdmin::class.java)
            startActivity(intent)
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
            val group = Grup(gambar, editNama.text.toString(),kategori, currEmail)

            group.nama?.let { it1 ->
                db.collection("tbGrup").document(nama).set(group)
                    .addOnSuccessListener {
                        editNama.setText("")



                        Toast.makeText(this, "Edit data berhasil", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@EditGroup, MainAdmin::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal melakukan edit data", Toast.LENGTH_LONG).show()
                    }
            }

        }
    }


    private fun addUploadRecordToDb(uri: String) {
        val group =
            Grup(uri, editNama.text.toString(),kategori, currEmail)

        group.nama?.let { it1 ->
            db.collection("tbGrup").document(nama).set(group)
                .addOnSuccessListener {
                    editNama.setText("")



                    Toast.makeText(this, "Edit data berhasil", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@EditGroup, MainAdmin::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal melakukan edit data", Toast.LENGTH_LONG).show()
                }
        }
    }
}