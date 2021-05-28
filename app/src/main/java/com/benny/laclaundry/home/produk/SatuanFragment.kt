package com.benny.laclaundry.home.produk

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.jacksonandroidnetworking.JacksonParserFactory
import kotlinx.android.synthetic.main.fragment_kiloan.*
import org.json.JSONObject

class SatuanFragment : Fragment() {

    var arrayList = ArrayList<ModelProduk>()
    private lateinit var rvSatuan: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_satuan, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AndroidNetworking.setParserFactory(JacksonParserFactory())
        rvSatuan = view.findViewById(R.id.rvProdukSatuan)
        rvSatuan.setHasFixedSize(true)
        showRecyclerList()

        fabTambahProduk.setOnClickListener{
            val intent =  Intent(context, ManageProduk::class.java)
            intent.putExtra("jenisProduk","Satuan")
            startActivity(intent)
        }
    }
    private fun showRecyclerList() {
        rvSatuan.layoutManager = LinearLayoutManager(context)
        val kiloanAdapter = ProdukAdapter(context, arrayList)
        rvSatuan.adapter = kiloanAdapter
    }

    override fun onResume() {
        super.onResume()
        loadAllProduct()
    }

    private fun loadAllProduct() {
        val loading = ProgressDialog(context)
        loading.setMessage("Memuat data...")
        loading.show()

        val preferences = this.activity
            ?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id: Int = preferences!!.getInt("id",0)

        AndroidNetworking.get(ApiEndPoint.READ_SATUAN+"?idUser="+ id)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object  : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    arrayList.clear()
                    val jsonArray = response?.optJSONArray("result")

                    if(jsonArray?.length() == 0 ){
                        loading.dismiss()
                        Toast.makeText(context, "Data Produk Kosong, Tambahkan data terlebih dahulu", Toast.LENGTH_LONG).show()
                    }

                    for(i in 0 until jsonArray?.length()!!){
                        val jsonObject = jsonArray?.optJSONObject(i)

                        arrayList.add(ModelProduk(jsonObject.getInt("ID_Produk"),
                            jsonObject.getInt("ID_User"),
                            jsonObject.getString("Jenis"),
                            jsonObject.getString("Nama_Produk"),
                            jsonObject.getInt("Harga_Produk")))

                        if(jsonArray?.length() - 1 == i){
                            loading.dismiss()
                            val adapter = ProdukAdapter(context, arrayList)
                            adapter.notifyDataSetChanged()
                            rvSatuan.adapter  = adapter
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