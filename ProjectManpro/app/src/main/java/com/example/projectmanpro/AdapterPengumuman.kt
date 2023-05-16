package com.example.projectmanpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterPengumuman (
    private val listPengumuman: ArrayList<Pengumuman>
): RecyclerView.Adapter<AdapterPengumuman.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var judul: TextView = itemView.findViewById((R.id.textViewJudul))
        var general: TextView = itemView.findViewById(R.id.textViewGeneral)
        var isiPengumuman: TextView = itemView.findViewById(R.id.textViewPengumuman)
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
    }

    override fun getItemCount(): Int {
        return listPengumuman.size
    }
}