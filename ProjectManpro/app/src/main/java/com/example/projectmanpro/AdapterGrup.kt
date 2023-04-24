package com.example.projectmanpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterGrup(


    private val listGrup: ArrayList<Grup>
    ): RecyclerView.Adapter<AdapterGrup.ListViewHolder>()
    {
        inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var nama: TextView = itemView.findViewById((R.id.textViewNama))
            var kategori: TextView = itemView.findViewById(R.id.textViewKategori)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val view: View =LayoutInflater.from(parent.context).inflate(R.layout.rv_grup, parent, false)
            return ListViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            var pengumuman =listGrup[position]
            holder.nama.setText(pengumuman.nama)
            holder.kategori.setText(pengumuman.kategori)

        }

        override fun getItemCount(): Int {
            return listGrup.size
        }


    }