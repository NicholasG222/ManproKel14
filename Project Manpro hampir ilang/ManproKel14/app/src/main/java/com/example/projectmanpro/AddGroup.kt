package com.example.projectmanpro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class AddGroup : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    private lateinit var inputTitle: EditText
    private lateinit var inputCategory: Spinner
    private val PICK_IMAGE_REQUEST = 71
    private lateinit var inputImage: ImageView
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private lateinit var date: String
    private lateinit var kategori: String
    private lateinit var currRole: String
    private lateinit var currEmail: String
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        val buttonAdd = findViewById<Button>(R.id.buttonAddGroup2)
        inputTitle = findViewById<EditText>(R.id.editTextTitleG)
        inputCategory = findViewById<Spinner>(R.id.spinnerCategoryG)
        inputImage = findViewById(R.id.imageView)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        var sp = getSharedPreferences("dataSP", MODE_PRIVATE)

        currRole = sp.getString("spRole", null)!!

        currEmail = sp.getString("spRegister", null)!!

        db = FirebaseFirestore.getInstance()
        var adapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, resources
                .getStringArray(R.array.group_category)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputCategory.adapter = adapter
        kategori = "HIMA"
        val context = this
        inputCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 1) {
                    kategori = "Kelas"
                    if(currRole == "HIMA"){
                        Toast.makeText(context, "Tidak bisa mengakses kategori kelas", Toast.LENGTH_LONG).show()
                        inputCategory.setSelection(0)
                    }
                } else if (p2 == 2) {
                    kategori = "Tugas akhir"
                    if(currRole != "Kaprodi" && currRole != "Koordinator skripsi"){
                        Toast.makeText(context, "Tidak bisa mengakses kategori TA", Toast.LENGTH_LONG).show()
                        inputCategory.setSelection(0)
                    }
                } else if (p2 == 3) {
                    kategori = "LEAP"
                    if(currRole != "Kaprodi" && currRole != "Koordinator skripsi" && currRole != "Wakil kaprodi" && currRole != "Sekretaris" ){
                        Toast.makeText(context, "Tidak bisa mengakses kategori LEAP", Toast.LENGTH_LONG).show()
                        inputCategory.setSelection(0)
                    }
                } else if (p2 == 4) {
                    kategori = "Bimbingan TA"
                    if(currRole != "Kaprodi" && currRole != "Koordinator skripsi" && currRole != "Wakil kaprodi" && currRole != "Sekretaris" && currRole != "Dosen"){
                        Toast.makeText(context, "Tidak bisa mengakses kategori bimbingan", Toast.LENGTH_LONG).show()
                        inputCategory.setSelection(0)
                    }
                } else if (p2 == 5) {
                    kategori = "Laboratorium"
                    if(currRole != "Asisten dosen" && currRole != "Asisten lab"){
                        Toast.makeText(context, "Tidak bisa mengakses kategori laboratorium", Toast.LENGTH_LONG).show()
                        inputCategory.setSelection(0)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                kategori = "HIMA"
            }
        }
        inputImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Pilih gambar"),
                PICK_IMAGE_REQUEST
            )
        }


        buttonAdd.setOnClickListener {

            uploadImage()
            var roleList = ""
            var kat = kategori
            if (kat == "HIMA") {
                Log.d("test", "sukses")
                roleList = roleList.plus(" HIMA")
            } else if (kat == "Tugas akhir") {
                roleList = roleList.plus(" Koordinator skripsi")
            } else if (kat == "LEAP") {

                roleList = roleList.plus(" Kaprodi")
                roleList = roleList.plus(" Wakil kaprodi")
                roleList = roleList.plus(" Sekretaris")
                roleList = roleList.plus(" Koordinator skripsi")
            } else if (kat == "Kelas" || kat == "Laboratorium" || kat == "Bimbingan TA") {
                roleList = roleList.plus(" " + sp.getString("spRegister", null))
            }

            var roleEditor = GrupEditorRole(kat, roleList)
            db.collection("tbEditorRole").document(kategori.toString()).set(roleEditor)
                .addOnSuccessListener {
                    Toast.makeText(this, "Role editor berhasil diatur", Toast.LENGTH_LONG).show()
                }



            Toast.makeText(this, "Simpan data berhasil", Toast.LENGTH_LONG).show()


            val eIntent = Intent(this@AddGroup, MainAdmin::class.java)
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
                inputImage.setImageBitmap(bitmap)
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
                    Log.e("it", it.toString())
                    Toast.makeText(this, "Gagal upload image", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Image tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addUploadRecordToDb(uri: String) {
        val group =
            Grup(uri, inputTitle.text.toString(),kategori, currEmail,inputTitle.text.toString())

        group.nama?.let { it1 ->
            db.collection("tbGrup").document(group.nama!!).set(group)
                .addOnSuccessListener {
                    inputTitle.setText("")



                    Toast.makeText(this, "Simpan data berhasil", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_LONG).show()
                }
        }
    }





}