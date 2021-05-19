package com.benny.laclaundry.home.pelanggan

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.benny.laclaundry.R

class PelangganAdapter(private val context: Context, private val arrayList: ArrayList<ModelPelanggan>): RecyclerView.Adapter<PelangganAdapter.ViewHolder>(){
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var namaPelanggan: TextView = view.findViewById(R.id.txtNamaPelanggan)
        var alamat: TextView = view.findViewById(R.id.txtAlamat)
        var nohp: TextView = view.findViewById(R.id.txtNohp)
        var cvPelanggan: CardView = view.findViewById(R.id.cvList_pelanggan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pelanggan_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = arrayList[position]
        holder.namaPelanggan.text = pelanggan.namaPelanggan
        holder.alamat.text = pelanggan.alamat
        holder.nohp.text = pelanggan.nohp

        holder.cvPelanggan.setOnClickListener {
            val intent = Intent(context, ManagePelanggan::class.java)
            intent.putExtra("editmode","1")
            intent.putExtra("idPelanggan",pelanggan.idPelanggan)
            intent.putExtra("namaPelanggan", pelanggan.namaPelanggan)
            intent.putExtra("nohp", pelanggan.nohp)
            intent.putExtra("alamat", pelanggan.alamat)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}