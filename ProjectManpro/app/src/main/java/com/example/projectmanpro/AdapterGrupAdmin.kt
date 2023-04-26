package com.example.projectmanpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterGrupAdmin(


    private val listGrup: ArrayList<Grup>
): RecyclerView.Adapter<AdapterGrupAdmin.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nama: TextView = itemView.findViewById((R.id.textViewNama))
        var kategori: TextView = itemView.findViewById(R.id.textViewKategori)
        var _buttonDelete:Button = itemView.findViewById(R.id.buttonDelete)
        var _buttonEdit:Button = itemView.findViewById(R.id.buttonEdit)

    }


    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun pindahEdit(data : Grup)
        fun deleteGrup(data: Grup)
    }


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =LayoutInflater.from(parent.context).inflate(R.layout.rv_grup, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var grup =listGrup[position]
        holder.nama.setText(grup.nama)
        holder.kategori.setText(grup.kategori)

        holder._buttonDelete.setOnClickListener {
            onItemClickCallback.deleteGrup(listGrup[position])
        }

        holder._buttonEdit.setOnClickListener {
            onItemClickCallback.pindahEdit(listGrup[position])
        }

    }

    override fun getItemCount(): Int {
        return listGrup.size
    }


}