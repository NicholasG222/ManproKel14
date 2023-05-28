package com.example.projectmanpro

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage

class AdapterGrupAdmin(


    private val listGrup: ArrayList<Grup>
): RecyclerView.Adapter<AdapterGrupAdmin.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nama: TextView = itemView.findViewById((R.id.textViewNama))
        var kategori: TextView = itemView.findViewById(R.id.textViewKategori)
        var buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
        var buttonEdit: Button = itemView.findViewById(R.id.buttonEdit)
        var foto: ImageView = itemView.findViewById(R.id.imageViewLogo)
    }


    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun pindahEdit(data : Grup)
        fun deleteGrup(data: Grup)
        fun imageClicked(data: Grup)
    }


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =LayoutInflater.from(parent.context).inflate(R.layout.rv_group_admin, parent, false)
        return ListViewHolder(view)
    }



    override fun getItemCount(): Int {
        return listGrup.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var grup =listGrup[position]
        holder.nama.setText(grup.nama)
        holder.kategori.setText(grup.kategori)
        val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(grup.gambar!!)
        imageRef.getBytes(100*100*1024).addOnSuccessListener {
            val bitmap =  BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.foto.setImageBitmap(bitmap)

        }

        holder.buttonDelete.setOnClickListener {
            onItemClickCallback.deleteGrup(listGrup[position])

        }

        holder.buttonEdit.setOnClickListener {
            onItemClickCallback.pindahEdit(listGrup[position])
        }
        holder.foto.setOnClickListener {
            onItemClickCallback.imageClicked(listGrup[position])
        }
    }


}