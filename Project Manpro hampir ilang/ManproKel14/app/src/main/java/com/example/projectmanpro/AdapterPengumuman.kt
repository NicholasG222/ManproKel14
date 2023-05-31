package com.example.projectmanpro

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.coroutineContext

class AdapterPengumuman (
    private val listPengumuman: ArrayList<Pengumuman>
): RecyclerView.Adapter<AdapterPengumuman.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var judul: TextView = itemView.findViewById((R.id.textViewJudul))
        var general: TextView = itemView.findViewById(R.id.textViewGeneral)
        var isiPengumuman: TextView = itemView.findViewById(R.id.textViewPengumuman)
        var imagePicker: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =LayoutInflater.from(parent.context).inflate(R.layout.rv_pengumuman, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var pengumuman = listPengumuman[position]
        holder.judul.setText(pengumuman.judul)
        holder.general.setText(pengumuman.date)
        holder.isiPengumuman.setText(pengumuman.isi)
        if(pengumuman.image != "none") {
            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(pengumuman.image!!)
            imageRef.getBytes(100 * 100 * 1024).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                holder.imagePicker.setImageBitmap(bitmap)

            }
        }else{
            holder.imagePicker.isVisible = false
        }
    }

    override fun getItemCount(): Int {
        return listPengumuman.size
    }
}