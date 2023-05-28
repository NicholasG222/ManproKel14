package com.example.projectmanpro

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanpro.DateHelper.getCurrentDate
import com.example.projectmanpro.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*

class ChatGroupActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance();
    private var listPengumuman = arrayListOf<pengumumangrup>()
    private lateinit var  buttonSend: FloatingActionButton
    private lateinit var inputJudul: EditText
    private lateinit var inputChat: EditText
    private lateinit var rvPengumumanGrup: RecyclerView
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null

    lateinit var dataIntent: String
    lateinit var dataRole: String
    lateinit var currentRole: String
    lateinit var currEmail: String
    lateinit var sp: SharedPreferences
    lateinit var image: ImageView
    private var adapterPG = adapterPengumumanGrup(listPengumuman)
    private lateinit var date: String
    private var storageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_group)
        date = getCurrentDate()
        rvPengumumanGrup = findViewById(R.id.rvChatGrup)
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        buttonSend = findViewById<FloatingActionButton>(R.id.fabSend)
        image = findViewById(R.id.imageView)
        inputJudul = findViewById<EditText>(R.id.editJudulChat)
        inputChat = findViewById<EditText>(R.id.editChat)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        dataIntent = intent.getStringExtra(data)!!
        dataRole = intent.getStringExtra(data2)!!
        buttonSend.isVisible = false
        inputChat.isVisible = false
        inputJudul.isVisible = false
        image.isVisible = false
        currentRole = sp.getString("spRole", null)!!

        currEmail = sp.getString("spRegister", null)!!

        var arrayRole = arrayListOf<String>()

        db.collection("tbDoubleRole").get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    var email = doc.getString("email")
                    var role = doc.getString("nama")
                    if (email == currEmail) {
                        arrayRole.add(role!!)

                    }

                }
                if(dataRole == "LEAP"){
                    if(arrayRole.contains("Kaprodi") || arrayRole.contains("Wakil kaprodi") || arrayRole.contains("Sekretaris") || arrayRole.contains("Koordinator skripsi")){

                        buttonSend.isVisible = true
                        inputChat.isVisible = true
                        inputJudul.isVisible = true
                        image.isVisible = true
                    }
                }else if(dataRole == "Tugas akhir"){
                    if(arrayRole.contains("Kaprodi") || arrayRole.contains("Koordinator skripsi")){

                        buttonSend.isVisible = true
                        inputChat.isVisible = true
                        inputJudul.isVisible = true
                        image.isVisible = true
                    }

                }else if(dataRole == "HIMA"){
                    if(arrayRole.contains("HIMA")){

                        buttonSend.isVisible = true
                        inputChat.isVisible = true
                        inputJudul.isVisible = true
                        image.isVisible = true
                    }
                }

            }
        if (dataRole == "Kelas" || dataRole == "Laboratorium" || dataRole == "Bimbingan TA") {
            db.collection("tbGrup").document(dataIntent!!).get()
                .addOnSuccessListener { result ->
                    val editorRole = result.getString("createdBy")

                    if (editorRole == currEmail) {

                        buttonSend.isVisible = true
                        inputChat.isVisible = true
                        inputJudul.isVisible = true
                        image.isVisible = true

                    }
                }
        } else {
            db.collection("tbEditorRole").document(dataRole!!).get()
                .addOnSuccessListener { result ->
                    val editorRole = result.getString("editor")

                    if (editorRole!!.contains(currentRole!!)) {

                        buttonSend.isVisible = true
                        inputChat.isVisible = true
                        inputJudul.isVisible = true
                        image.isVisible = true


                    }
                }
        }
        if(currentRole == "Super Admin"){
            buttonSend.isVisible = true
            inputChat.isVisible = true
            inputJudul.isVisible = true
            image.isVisible = true
        }




        SiapkanData()
        buttonSend.setOnClickListener {
           uploadImage()
        }
        image.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Pilih gambar"),
                PICK_IMAGE_REQUEST
            )
        }
        buttonBack.setOnClickListener {
            if (currentRole != "user") {
                val intent = Intent(this@ChatGroupActivity, MainAdmin::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@ChatGroupActivity, HomeUser::class.java)
                startActivity(intent)
            }
        }
    }

    private fun SiapkanData() {
        db.collection("tbPengumumanGrup")
            .get()
            .addOnSuccessListener { result ->


                for (document in result) {
                    if (document.getString("grup") == dataIntent) {
                        var pengumumangrup = pengumumangrup(
                            document.getString("gambar"),
                            document.getString("pengirim"),
                            document.getString("judul"),
                            document.getString("isi"),
                            document.getString("grup"),
                            document.getString("date")
                        )
                        listPengumuman.add(pengumumangrup)
                    }


                }
                rvPengumumanGrup.layoutManager = LinearLayoutManager(this)

                rvPengumumanGrup.adapter = adapterPG
                setCallbackPG()
            }
    }

    companion object {
        const val data = "data"
        const val data2 = "data2"
    }
    private fun setCallbackPG(){
        adapterPG.setOnItemClickCallback(object: adapterPengumumanGrup.OnItemClickCallback {
            override fun delAnn(data: pengumumangrup) {
                AlertDialog.Builder(this@ChatGroupActivity).setTitle("Hapus pengumuman")
                    .setMessage("Apakah Anda ingin menghapus pengumuman?")
                    .setPositiveButton(
                        "HAPUS",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            db.collection("tbPengumumanGrup").document(data.isi!!).delete()
                            listPengumuman.remove(data)
                            adapterPG.notifyDataSetChanged()


                        }).setNegativeButton(
                        "BATAL",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            Toast.makeText(
                                this@ChatGroupActivity,
                                "BATAL",
                                Toast.LENGTH_SHORT
                            ).show()

                        }).show()
            }
        })
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
                image.setImageBitmap(bitmap)
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
        var pgrup = pengumumangrup(
            uri,
            currEmail,
            inputJudul.text.toString(),
            inputChat.text.toString(),
            dataIntent,
            date
        )
        db.collection("tbPengumumanGrup").document(pgrup.isi!!).set(pgrup)
            .addOnSuccessListener {
                Toast.makeText(
                    this@ChatGroupActivity, "Pengumuman berhasil ditambahkan",
                    Toast.LENGTH_SHORT
                ).show()
                inputJudul.setText("")
                inputChat.setText("")
                image.setImageResource(R.drawable.ic_baseline_image_24)
            }

    }





}