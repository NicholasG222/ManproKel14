package com.example.projectmanpro

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class adapterPengumumanGrup(
    private val listPengumuman: ArrayList<pengumumangrup>, private val bool: Boolean
):RecyclerView.Adapter<adapterPengumumanGrup.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tanggal: TextView = itemView.findViewById(R.id.tanggalPengumuman)
        var _isiPengumuman: TextView = itemView.findViewById(R.id.isiPengumuman)
        var _gambar: ImageView = itemView.findViewById(R.id.gambar)

        var _nama: TextView = itemView.findViewById(R.id.namaPengirim)
        var judul: TextView = itemView.findViewById(R.id.judul)
        val delete: Button = itemView.findViewById(R.id.buttonDelete)
    }
    private lateinit var onItemClickCallback: OnItemClickCallback


    interface OnItemClickCallback {

        fun delAnn(data: pengumumangrup)
    }
    fun setOnItemClickCallback(onItemClickCallback: adapterPengumumanGrup.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.received_chat, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var grupPengumuman = listPengumuman[position]
        holder.delete.isVisible = bool

        holder._nama.setText(grupPengumuman.pengirim)
        holder._isiPengumuman.setText(grupPengumuman.isi)
        holder.judul.setText(grupPengumuman.judul)
        holder._tanggal.setText(grupPengumuman.date)
        if(grupPengumuman.gambar != "none") {
            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(grupPengumuman.gambar!!)
            imageRef.getBytes(100 * 100 * 1024).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                holder._gambar.setImageBitmap(bitmap)

            }
        }else{
            holder._gambar.isVisible = false
        }
        holder.delete.setOnClickListener {
            onItemClickCallback.delAnn(grupPengumuman)
        }

    }

    override fun getItemCount(): Int {
        return listPengumuman.size
    }


}