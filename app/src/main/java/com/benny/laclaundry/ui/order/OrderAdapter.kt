package com.benny.laclaundry.ui.order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.benny.laclaundry.R
import kotlinx.android.synthetic.main.item_order.view.*

class OrderAdapter(private val context: Context?,  private val arrayList: ArrayList<ModelOrder>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtOrderPelanggan: TextView = itemView.findViewById(R.id.txtOrderPelanggan)
        var txtOrderProduk: TextView = itemView.findViewById(R.id.txtOrderProduk)
        var txtOrderMasuk: TextView = itemView.findViewById(R.id.txtOrderMasuk)
        var txtOrderKeluar: TextView = itemView.findViewById(R.id.txtOrderSelesai)
        var txtOrderTotalBayar: TextView = itemView.findViewById(R.id.txtOrderTotal)
        var txtOrderStatusBayar: TextView = itemView.findViewById(R.id.txtOrderStatus)
        var cvOrder: CardView = itemView.findViewById(R.id.cvOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = arrayList[position]
        holder.txtOrderPelanggan.text = order.namaPelanggan
        holder.txtOrderProduk.text = order.namaProduk
        holder.txtOrderMasuk.text = order.tglMasuk
        holder.txtOrderKeluar.text = order.tglSelesai
        holder.txtOrderTotalBayar.text = order.totalDibayar.toString()
        holder.txtOrderStatusBayar.text = order.statusBayar
        if(order.statusBayar.contains("Belum Lunas")){
            holder.txtOrderStatusBayar.setTextColor(Color.RED)
        }
        holder.cvOrder.setOnClickListener {
            val intent = Intent(context, ManageOrder::class.java)
            intent.putExtra("idTransaksi", order.idTransaksi)
            intent.putExtra("idPelanggan", order.idPelanggan)
            intent.putExtra("namaPelanggan", order.namaPelanggan)
            intent.putExtra("alamatPelanggan", order.namaPelanggan)
            intent.putExtra("nohp", order.nohp)
            intent.putExtra("idProduk", order.idProduk)
            intent.putExtra("namaProduk", order.namaProduk)
            intent.putExtra("tglMasuk", order.tglMasuk)
            intent.putExtra("tglSelesai", order.tglSelesai)
            intent.putExtra("metodeBayar", order.metodeBayar)
            intent.putExtra("jumlahBayar", order.jumlahBayar)
            intent.putExtra("totalDibayar", order.totalDibayar)
            intent.putExtra("catatan", order.catatan)
            intent.putExtra("statusBayar", order.statusBayar)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}