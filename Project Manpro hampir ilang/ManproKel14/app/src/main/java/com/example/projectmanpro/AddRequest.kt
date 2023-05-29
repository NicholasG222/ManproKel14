package com.example.projectmanpro

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class AddRequest : AppCompatActivity() {
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_request)
        var spinner = findViewById<Spinner>(R.id.spinnerRole)
        val db = FirebaseFirestore.getInstance()
        var editNote = findViewById<EditText>(R.id.editNotes)
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        var role = "Dosen"
        val isiSP = sp.getString("spRegister", null)
        var buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        var buttonBack = findViewById<Button>(R.id.buttonBack)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, resources
            .getStringArray(R.array.role_array))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2 == 1){
                    role = "Sekretaris"
                }else if(p2 == 2){
                    role = "Asisten dosen"
                }else if(p2 == 3){
                    role = "Asisten lab"
                }else if(p2 == 4){
                    role = "Dosen luar biasa"
                }else if(p2 == 5){
                    role = "Staff"
                }else if(p2 == 6){
                    role = "Wakil kaprodi"
                }else if(p2 == 7){
                    role = "HIMA"
                }
                else if(p2 == 8){
                    role = "Kaprodi"
                }
                else if(p2 == 9){
                    role = "Koordinator skripsi"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                role = "Dosen"
            }



        }
        buttonSubmit.setOnClickListener {
            AlertDialog.Builder(this@AddRequest).setTitle("Request access as admin")
                .setMessage("Apakah ingin meminta akses menjadi admin?")
                .setPositiveButton(
                    "REQUEST",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        var note = editNote.text.toString()
                        var request = AdminAccessRequests(isiSP, role, note)
                        db.collection("tbRequests").document(isiSP.toString()).set(request)

                    }).setNegativeButton(
                    "BATAL",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        Toast.makeText(
                            this@AddRequest,
                            "BATALKAN REQUEST",
                            Toast.LENGTH_SHORT
                        ).show()

                    }).show()

        }
        buttonBack.setOnClickListener {
            val intent = Intent(this@AddRequest, HomeUser::class.java)
            startActivity(intent)
        }
    }
}