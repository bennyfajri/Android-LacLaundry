package com.benny.laclaundry.ui.order

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.benny.laclaundry.home.produk.ApiProduk
import kotlinx.android.synthetic.main.activity_konfirmasi_pesanan.*
import kotlinx.android.synthetic.main.activity_manage_order.*
import kotlinx.android.synthetic.main.activity_manage_order.txtJumlahBayar
import kotlinx.android.synthetic.main.activity_manage_produk.*
import kotlinx.android.synthetic.main.fragment_orderan.*
import kotlinx.android.synthetic.main.product_list.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ManageOrder : AppCompatActivity() {

    lateinit var i: Intent
    lateinit var sp: SharedPreferences
    var idTransaksi = 0
    lateinit var idPelanggan: String
    lateinit var namaPelanggan: String
    lateinit var alamatPelanggan: String
    lateinit var nohp: String
    lateinit var idProduk: String
    lateinit var namaProduk: String
    lateinit var tglMasuk: String
    lateinit var tglSelesai: String
    lateinit var metodeBayar: String
    var jumlah = 0
    var jumlahBayar = 0
    var totalBayar = 0
    var hargaProduk = 0
    lateinit var catatan: String
    lateinit var statusBayar: String
    lateinit var namaLaundry: String
    lateinit var alamatLaundry: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_order)

        getIntentValue()
        setViewValue()
        makeQrCode()


        btnEditOrder.setOnClickListener {
            editOrder()
        }


        btnKirimNota.setOnClickListener {
            kirimNota()
        }
    }

    private fun makeQrCode() {
        val text = "$namaLaundry\n $alamatLaundry\n==============\n" +
                "Nama: $namaPelanggan\nAamat : $alamatPelanggan\nNo HP : $nohp\nTanggal : $tglMasuk\nEstimasi: $tglSelesai\n==============\n" +
                "Pesanan : $namaProduk\nHarga: $jumlahBayar\nBerat(Kg) / Jumlah(Pcs) : $jumlah\nTotal: $jumlahBayar"
    }

    private fun getIntentValue() {
        var format = SimpleDateFormat("dd-mm-yyyy hh:mm:ss")
        var format1 = SimpleDateFormat("dd-mm-yyyy")

        i = intent
        sp = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        idTransaksi = i.getIntExtra("idTransaksi",0)
        idPelanggan = i.getStringExtra("idPelanggan").toString()
        namaPelanggan = i.getStringExtra("namaPelanggan").toString()
        alamatPelanggan = i.getStringExtra("alamatPelanggan").toString()
        nohp = i.getStringExtra("nohp").toString()
        idProduk = i.getStringExtra("idProduk").toString()
        namaProduk = i.getStringExtra("namaProduk").toString()
        hargaProduk = i.getIntExtra("hargaProduk",0)


        val tglMasukParse = i.getStringExtra("tglMasuk").toString()
        val date1: Date = format.parse(tglMasukParse)
        format = SimpleDateFormat("dd-MMM-yy hh:mm:ss a")
        tglMasuk = format.format(date1)


        val tglKeluarParse = i.getStringExtra("tglSelesai").toString()
        val date2: Date = format1.parse(tglKeluarParse)
        format1 = SimpleDateFormat("dd-MMM-yyyy")
        tglSelesai = format1.format(date2)

        metodeBayar = i.getStringExtra("metodeBayar").toString()
        jumlah = i.getIntExtra("jumlah",0)
        jumlahBayar = i.getIntExtra("jumlahBayar",0)
        totalBayar = i.getIntExtra("totalBayar",0)
        catatan = i.getStringExtra("catatan").toString()
        statusBayar = i.getStringExtra("statusBayar").toString()
        namaLaundry = sp.getString("namaLaundry", "").toString()
        alamatLaundry = sp.getString("alamatLaundry", "").toString()
    }

    private fun setViewValue() {
        txtOrderNamaPel.setText(namaPelanggan)
        txtOrderNOHP.setText(nohp)
        txtOrderAlamat.setText(alamatPelanggan)
        txtOrderNamaPr.setText(namaProduk)
        txtOrderJumlah.setText(jumlah.toString())
        txtOrderJumlahBayar.setText("Rp. ${jumlahBayar.toString()}")
        txtOrderHargaPr.setText("Rp. ${hargaProduk.toString()}")
        txtOrderTglMasuk.setText(tglMasuk)
        txtOrderTglSelesai.setText(tglSelesai)
        etOrderCatatan.setText(catatan)
        txtOrderTotalBayar.setText("Rp. ${jumlahBayar.toString()}")
        if (statusBayar?.equals("Belum Lunas")){
            spinStatusBayar.visibility = View.VISIBLE
            layoutEtOrderJumlah.visibility = View.VISIBLE
            etOrderTotalBayar.visibility = View.VISIBLE
            txtOrderStatusBayar.visibility = View.GONE
            txtOrderTotalBayar.visibility = View.GONE
        }
    }

    private fun kirimNota() {

    }

    private fun editOrder() {
        val totalBayar =  etOrderTotalBayar.text.toString()
        val statusBayar = spinStatusBayar.selectedItem.toString()
        val idUser = sp.getInt("id",0).toString()
//        Toast.makeText(applicationContext, "ID Transaksi : $idTransaksi,ID User: $idUser, Total Bayar : $totalBayar, status bayar : $statusBayar", Toast.LENGTH_LONG).show()
        val loading= ProgressDialog(this)
        loading.setMessage("Mengubah data..")
        loading.show()

        AndroidNetworking.post(ApiOrder.UPDATE)
            .addBodyParameter("idTrans", idTransaksi.toString())
            .addBodyParameter("idUser",idUser)
            .addBodyParameter("totalBayar",totalBayar)
            .addBodyParameter("statusBayar",statusBayar)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()

                    if(response?.getString("message")?.contains("successfully")!!){
                        this@ManageOrder.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_LONG).show()
                }

            })

    }


}