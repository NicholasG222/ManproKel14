package com.example.projectmanpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

class adapterRequest (
   private val listRequest: ArrayList<AdminAccessRequests>
): RecyclerView.Adapter<adapterRequest.ListViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var email: TextView = itemView.findViewById(R.id.textViewEmail)
        var role: TextView = itemView.findViewById(R.id.textViewRole)
        var catatan: TextView = itemView.findViewById(R.id.textViewNotes)
        var reject: FloatingActionButton = itemView.findViewById(R.id.crossButton)
        var accept: FloatingActionButton = itemView.findViewById(R.id.checkButton)
    }
    interface OnItemClickCallback{
        fun accept(data: AdminAccessRequests)
        fun reject(data: AdminAccessRequests)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_request, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       var request = listRequest[position]
        holder.email.setText(request.email)
        holder.role.setText(request.role)
        holder.catatan.setText(request.catatan)
        holder.accept.setOnClickListener {
            onItemClickCallback.accept(request)
        }
        holder.reject.setOnClickListener {
            onItemClickCallback.reject(request)
        }
    }

    override fun getItemCount(): Int {
        return listRequest.size
    }

}