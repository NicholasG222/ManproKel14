package com.example.projectmanpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class adapterPengumumanGrup(
    private val listPengumuman: ArrayList<pengumumangrup>
):RecyclerView.Adapter<adapterPengumumanGrup.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tanggal: TextView = itemView.findViewById(R.id.tanggalPengumuman)
        var _isiPengumuman: TextView = itemView.findViewById(R.id.isiPengumuman)
        var _gambar: ImageView = itemView.findViewById(R.id.gambar)
        var _nama: TextView = itemView.findViewById(R.id.namaPengirim)
        var judul: TextView = itemView.findViewById(R.id.judul)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.received_chat, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var grupPengumuman = listPengumuman[position]

        holder._nama.setText(grupPengumuman.pengirim)
        holder._isiPengumuman.setText(grupPengumuman.isi)
       holder.judul.setText(grupPengumuman.judul)

    }

    override fun getItemCount(): Int {
        return listPengumuman.size
    }


}