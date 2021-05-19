package com.benny.laclaundry.home.produk

import android.app.ProgressDialog
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

class KiloanFragment : Fragment() {

    var arrayList = ArrayList<ModelProduk>()
    private lateinit var rvKiloan: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kiloan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AndroidNetworking.setParserFactory(JacksonParserFactory())
        rvKiloan = view.findViewById(R.id.rvProdukKiloan)
        rvKiloan.setHasFixedSize(true)
        showRecyclerList()

        fabTambahProduk.setOnClickListener{
            startActivity(Intent(context, ManageProduk::class.java))
        }
    }
    private fun showRecyclerList() {
        rvKiloan.layoutManager = LinearLayoutManager(context)
        val kiloanAdapter = ProdukAdapter(context, arrayList)
        rvKiloan.adapter = kiloanAdapter
    }

    override fun onResume() {
        super.onResume()
        loadAllProduct()
    }

    private fun loadAllProduct() {
        val loading = ProgressDialog(context)
        loading.setMessage("Memuat data...")
        loading.show()

        AndroidNetworking.get(ApiEndPoint.READ_KILOAN)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object  : JSONObjectRequestListener{
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
                            rvKiloan.adapter  = adapter
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