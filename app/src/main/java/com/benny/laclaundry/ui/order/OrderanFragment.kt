package com.benny.laclaundry.ui.order

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.benny.laclaundry.home.produk.ManageProduk
import com.benny.laclaundry.home.produk.ModelProduk
import com.benny.laclaundry.home.produk.ProdukAdapter
import com.jacksonandroidnetworking.JacksonParserFactory
import kotlinx.android.synthetic.main.fragment_kiloan.*
import org.json.JSONObject

class OrderanFragment : Fragment() {

    var arrayList = ArrayList<ModelOrder>()
    private lateinit var rvOrder: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orderan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AndroidNetworking.setParserFactory(JacksonParserFactory())
        rvOrder = view.findViewById(R.id.rvOrderan)
        rvOrder.setHasFixedSize(true)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        rvOrder.layoutManager = LinearLayoutManager(context)
        val orderAdapter = OrderAdapter(requireContext(), arrayList)
        rvOrder.adapter = orderAdapter
    }

    override fun onResume() {
        super.onResume()
        loadAllOrder()
    }

    private fun loadAllOrder() {
        val loading = ProgressDialog(context)
        loading.setMessage("Memuat data..")
        loading.show()

        val preferences = this.activity
            ?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id: Int = preferences!!.getInt("id",0)

        AndroidNetworking.get(ApiOrder.READ+"?idUser="+id)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    arrayList.clear()
                    val jsonArray = response?.optJSONArray("result")

                    if(jsonArray?.length() == 0 ){
                        loading.dismiss()
                        Toast.makeText(context, "Data Produk Kosong, Tambahkan data terlebih dahulu", Toast.LENGTH_LONG).show()
                    }
                    for(i in 0 until jsonArray?.length()!!){
                        val jsonObject = jsonArray?.optJSONObject(i)

                        arrayList.add(
                            ModelOrder(jsonObject.getInt("ID_Transaksi"),
                            jsonObject.getInt("ID_Pelanggan"),
                                jsonObject.getString("Nama_Pelanggan"),
                                jsonObject.getString("Alamat"),
                                jsonObject.getString("NoHP"),
                                jsonObject.getInt("ID_Produk"),
                                jsonObject.getString("Nama_Produk"),
                                jsonObject.getString("Tgl_Masuk"),
                                jsonObject.getString("Tgl_Selesai"),
                                jsonObject.getString("Metode_Pembayaran"),
                                jsonObject.getInt("Jumlah_Harga"),
                                jsonObject.getInt("Total_Dibayar"),
                                jsonObject.getString("Catatan"),
                                jsonObject.getString("status_pembayaran"),
                            )
                        )

                        if(jsonArray?.length() - 1 == i){
                            loading.dismiss()
                            val adapter = OrderAdapter(context!!, arrayList)
                            adapter.notifyDataSetChanged()
                            rvOrder.adapter  = adapter
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.e("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(context, "Connection Failed", Toast.LENGTH_LONG).show()
                }

            })

    }
}