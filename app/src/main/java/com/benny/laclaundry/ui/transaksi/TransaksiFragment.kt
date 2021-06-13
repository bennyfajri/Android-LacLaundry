package com.benny.laclaundry.ui.transaksi

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.benny.laclaundry.home.pelanggan.ApiPelanggan
import com.benny.laclaundry.home.pelanggan.ManagePelanggan
import com.benny.laclaundry.home.pelanggan.ModelPelanggan
import com.benny.laclaundry.home.produk.ApiProduk
import com.benny.laclaundry.home.produk.ManageProduk
import com.benny.laclaundry.home.produk.ModelProduk
import com.jacksonandroidnetworking.JacksonParserFactory
import kotlinx.android.synthetic.main.activity_manage_produk.*
import kotlinx.android.synthetic.main.fragment_transaksi.*
import org.json.JSONObject


class TransaksiFragment : Fragment(R.layout.fragment_transaksi) {

    var arrayPelanggan = ArrayList<ModelPelanggan>()
    var arrayProduk = ArrayList<ModelProduk>()
    lateinit var spinPelanggan: Spinner
    lateinit var spinProduk: Spinner
    lateinit var sp: SharedPreferences
    lateinit var etHarga : EditText


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
        etHarga = view.findViewById(R.id.etHargaTrans)
        etHarga.setText("")

        txtTambahPelanggan.setOnClickListener {
            startActivity(Intent(context, ManagePelanggan::class.java))
        }
        txtTambahProduk.setOnClickListener {
            startActivity(Intent(context, ManageProduk::class.java))
        }
        btnTambahTrans.setOnClickListener {
            transaksiBaru()
        }
    }

    private fun transaksiBaru() {
        var pelanggan = spinPelanggan.selectedItem.toString()
        var produk = spinProduk.selectedItem.toString()
        var arrSplitPelanggan: List<String> = pelanggan.split(", ")
        var arrSplitProduk: List<String> = produk.split(", ")
        var jumlah = etHarga.text.toString().toInt() * etJumlahTransaksi.text.toString().toInt()
        val intent = Intent(context, KonfirmasiPesanan::class.java)
        intent.putExtra("hargaDibayar", jumlah)
        intent.putExtra("idPelanggan", arrSplitPelanggan[2])
        intent.putExtra("namaPelanggan", arrSplitPelanggan[0])
        intent.putExtra("idProduk", arrSplitProduk[2])
        intent.putExtra("namaProduk", arrSplitProduk[0])
        startActivity(intent)
        Toast.makeText(context, "jumlah : $jumlah", Toast.LENGTH_LONG ).show()
    }

    override fun onResume() {
        super.onResume()
        loadAllProduct()
        loadPelanggan()
    }

    private fun loadPelanggan() {
        val loading = ProgressDialog(context)
        loading.setMessage("Memuat Data...")
        loading.show()

        val preferences = this.activity
            ?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val id: Int = preferences!!.getInt("id", 0)

        AndroidNetworking.get(ApiPelanggan.READ + "?idUser=" + id)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    arrayPelanggan.clear()
                    val jsonArray = response?.optJSONArray("result")

                    if (jsonArray?.length() == 0) {
                        loading.dismiss()
                        Toast.makeText(
                            context,
                            "Data pelanggan kosong, tambah data pelanggan..",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    for (i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)

                        arrayPelanggan.add(
                            ModelPelanggan(
                                jsonObject.getInt("ID_Pelanggan"),
                                jsonObject.getInt("ID_User"),
                                jsonObject.getString("Nama_Pelanggan"),
                                jsonObject.getString("NoHP"),
                                jsonObject.getString("Alamat")
                            )
                        )

                        if (jsonArray?.length() - 1 == i) {
                            loading.dismiss()
                            var adapter: ArrayAdapter<ModelPelanggan> =
                                ArrayAdapter<ModelPelanggan>(
                                    context!!, android.R.layout.simple_spinner_item, arrayPelanggan
                                )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinPelanggan.adapter = adapter
                            spinPelanggan.setOnItemSelectedListener(object :
                                OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    val pelanggan: ModelPelanggan =
                                        parent.selectedItem as ModelPelanggan
                                    displayUserData(pelanggan)
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            })
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.e("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(context, "Connection Failure", Toast.LENGTH_LONG).show()
                }
            })
    }

    fun displayUserData(pelanggan: ModelPelanggan){

    }

    private fun loadAllProduct() {
            val loading = ProgressDialog(context)
            loading.setMessage("Memuat data...")
            loading.show()

            val preferences = this.activity
                ?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            val id: Int = preferences!!.getInt("id", 0)

            AndroidNetworking.get(ApiProduk.READ_SEMUA + "?idUser=" + id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        arrayProduk.clear()
                        val jsonArray = response?.optJSONArray("result")

                        if (jsonArray?.length() == 0) {
                            loading.dismiss()
                            Toast.makeText(
                                context,
                                "Data Produk Kosong, Tambahkan data terlebih dahulu",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        for (i in 0 until jsonArray?.length()!!) {
                            val jsonObject = jsonArray?.optJSONObject(i)

                            arrayProduk.add(
                                ModelProduk(
                                    jsonObject.getInt("ID_Produk"),
                                    jsonObject.getInt("ID_User"),
                                    jsonObject.getString("Jenis"),
                                    jsonObject.getString("Nama_Produk"),
                                    jsonObject.getInt("Harga_Produk")
                                )
                            )

                            if (jsonArray?.length() - 1 == i) {
                                loading.dismiss()
                                var adapterProduk: ArrayAdapter<ModelProduk> =
                                    ArrayAdapter<ModelProduk>(
                                        requireContext(),
                                        android.R.layout.simple_spinner_item, arrayProduk
                                    )
                                adapterProduk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinProduk.adapter = adapterProduk
                                spinProduk.setOnItemSelectedListener(object :
                                    OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>,
                                        view: View,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val produk: ModelProduk = parent.selectedItem as ModelProduk
                                        setHarga(produk)
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                                })
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

    private fun setHarga(produk: ModelProduk) {
       etHarga.setText(produk.hargaProduk.toString())
    }

}