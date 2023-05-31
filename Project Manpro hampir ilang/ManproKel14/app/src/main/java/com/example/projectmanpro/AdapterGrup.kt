package com.example.projectmanpro

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage

class AdapterGrup(


    private val listGrup: ArrayList<Grup>
    ): RecyclerView.Adapter<AdapterGrup.ListViewHolder>()
    {
        inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var nama: TextView = itemView.findViewById((R.id.textViewNama))
            var kategori: TextView = itemView.findViewById(R.id.textViewKategori)
            var foto: ImageView = itemView.findViewById(R.id.imageViewLogo)
        }
        private lateinit var onItemClickCallback: OnItemClickCallback

        interface OnItemClickCallback {
            fun imageClicked(data: Grup)

        }
        fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
            this.onItemClickCallback = onItemClickCallback
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val view: View =LayoutInflater.from(parent.context).inflate(R.layout.rv_grup, parent, false)
            return ListViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            var pengumuman =listGrup[position]
            holder.nama.setText(pengumuman.nama)
            holder.kategori.setText(pengumuman.kategori)
            if(pengumuman.gambar != "none") {
                val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(pengumuman.gambar!!)
                imageRef.getBytes(100 * 100 * 1024).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    holder.foto.setImageBitmap(bitmap)

                }
            }else{
                holder.foto.setImageResource(R.drawable.logopetra)
            }
            holder.foto.setOnClickListener {
                onItemClickCallback.imageClicked(listGrup[position])
            }

        }

        override fun getItemCount(): Int {
            return listGrup.size
        }


    }