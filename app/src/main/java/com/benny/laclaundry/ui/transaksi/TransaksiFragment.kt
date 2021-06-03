package com.benny.laclaundry.ui.transaksi

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.benny.laclaundry.home.pelanggan.ModelPelanggan
import com.benny.laclaundry.home.produk.ApiEndPoint
import com.benny.laclaundry.home.produk.ModelProduk
import com.benny.laclaundry.home.produk.ProdukAdapter
import com.jacksonandroidnetworking.JacksonParserFactory
import org.json.JSONObject


class TransaksiFragment : Fragment(R.layout.fragment_transaksi) {

    var arrayPelanggan = ArrayList<ModelPelanggan>()
    var arrayProduk = ArrayList<ModelProduk>()
    lateinit var spinPelanggan: Spinner
    lateinit var spinProduk: Spinner
    lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaksi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AndroidNetworking.setParserFactory(JacksonParserFactory())
        spinPelanggan = view.findViewById(R.id.spinPelanggan)
        spinProduk = view.findViewById(R.id.spinProduk)

        var adapterPelanggan: ArrayAdapter<ModelPelanggan> = ArrayAdapter<ModelPelanggan>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, arrayPelanggan
        )


        adapterPelanggan.setDropDownViewResource(android.R.layout.simple_spinner_item)


        spinPelanggan.adapter = adapterPelanggan


        spinPelanggan.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val pelanggan: ModelPelanggan = parent.selectedItem as ModelPelanggan
                displayUserData(pelanggan)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    fun displayUserData(pelanggan: ModelPelanggan){

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
                        arrayProduk.clear()
                        val jsonArray = response?.optJSONArray("result")

                        if(jsonArray?.length() == 0 ){
                            loading.dismiss()
                            Toast.makeText(context, "Data Produk Kosong, Tambahkan data terlebih dahulu", Toast.LENGTH_LONG).show()
                        }

                        for(i in 0 until jsonArray?.length()!!){
                            val jsonObject = jsonArray?.optJSONObject(i)

                            arrayProduk.add(ModelProduk(jsonObject.getInt("ID_Produk"),
                                jsonObject.getInt("ID_User"),
                                jsonObject.getString("Jenis"),
                                jsonObject.getString("Nama_Produk"),
                                jsonObject.getInt("Harga_Produk")))

                            if(jsonArray?.length() - 1 == i){
                                loading.dismiss()
//                                val adapter = ProdukAdapter(context, arrayProduk)
//                                adapter.notifyDataSetChanged()
//                                rvSatuan.adapter  = adapter
                                var adapterProduk: ArrayAdapter<ModelProduk> = ArrayAdapter<ModelProduk>(
                                    requireContext(),
                                    android.R.layout.simple_spinner_dropdown_item, arrayProduk
                                )
                                adapterProduk.setDropDownViewResource(android.R.layout.simple_spinner_item)
                                spinProduk.adapter = adapterProduk
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