package com.benny.laclaundry.home.produk

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.benny.laclaundry.R

class ProdukAdapter(private val context: Context?, private val arrayList: ArrayList<ModelProduk>) : RecyclerView.Adapter<ProdukAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName: TextView = itemView.findViewById(R.id.txtNamaProduk)
        var txtJenis: TextView = itemView.findViewById(R.id.txtJenisProduk)
        var txtHarga: TextView = itemView.findViewById(R.id.txtHargaProduk)
        var list: CardView = itemView.findViewById(R.id.cvList_produk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produk = arrayList[position]
        holder.txtName.text = produk.namaProduk
        holder.txtJenis.text = produk.jenis
        holder.txtHarga.text = produk.hargaProduk.toString()

        holder.list.setOnClickListener {
            val intent = Intent(context, ManageProduk::class.java)
            intent.putExtra("editmode","1")
            intent.putExtra("idProduk",produk.idProduk)
            intent.putExtra("jenis",produk.jenis)
            intent.putExtra("namaProduk",produk.namaProduk)
            intent.putExtra("harga",produk.hargaProduk)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = arrayList.size
}