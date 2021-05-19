package com.benny.laclaundry.home.pelanggan

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.jacksonandroidnetworking.JacksonParserFactory
import kotlinx.android.synthetic.main.activity_list_pelanggan.*
import org.json.JSONObject

class ListPelanggan : AppCompatActivity() {
    var arrayList = ArrayList<ModelPelanggan>()
    private lateinit var rvPelanggan: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pelanggan)

        AndroidNetworking.setParserFactory(JacksonParserFactory())

        rvPelanggan = findViewById(R.id.rvPelanggan)
        rvPelanggan.setHasFixedSize(true)
        showRecyclerList()
        loadAllPelanggan()

        fabTambahPelanggan.setOnClickListener{
            startActivity(Intent(this,ManagePelanggan::class.java))
        }
    }

    private fun showRecyclerList() {
        rvPelanggan.layoutManager = LinearLayoutManager(this)
        val pelangganAdapter = PelangganAdapter(applicationContext, arrayList)
        rvPelanggan.adapter = pelangganAdapter
    }

    override fun onResume() {
        super.onResume()
        loadAllPelanggan()
    }

    private fun loadAllPelanggan() {
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat Data...")
        loading.show()

        AndroidNetworking.get(ApiEndPoint.READ)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    arrayList.clear()
                    val jsonArray = response?.optJSONArray("result")

                    if(jsonArray?.length() == 0){
                        loading.dismiss()
                        Toast.makeText(applicationContext, "Data pelanggan kosong, tambah data pelanggan..",Toast.LENGTH_LONG).show()
                    }

                    for(i in 0 until  jsonArray?.length()!!){
                        val jsonObject = jsonArray?.optJSONObject(i)

                        arrayList.add(ModelPelanggan(jsonObject.getInt("ID_Pelanggan"),
                        jsonObject.getInt("ID_User"),
                        jsonObject.getString("Nama_Pelanggan"),
                        jsonObject.getString("NoHP"),
                        jsonObject.getString("Alamat")))

                        if(jsonArray?.length() - 1 == i){
                            loading.dismiss()
                            val adapter = PelangganAdapter(applicationContext, arrayList)
                            adapter.notifyDataSetChanged()
                            rvPelanggan.adapter = adapter

                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.e("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_LONG).show()
                }
            })
    }
}