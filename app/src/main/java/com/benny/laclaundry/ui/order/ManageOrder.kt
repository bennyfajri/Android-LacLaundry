package com.benny.laclaundry.ui.order

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_konfirmasi_pesanan.*
import kotlinx.android.synthetic.main.activity_manage_order.*
import kotlinx.android.synthetic.main.activity_manage_produk.*
import kotlinx.android.synthetic.main.fragment_orderan.*
import kotlinx.android.synthetic.main.product_list.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
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
    var totalDibayar = 0
    var hargaProduk = 0
    lateinit var catatan: String
    lateinit var statusBayar: String
    lateinit var namaLaundry: String
    lateinit var alamatLaundry: String
    lateinit var nota: String


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
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(nota, BarcodeFormat.QR_CODE, 350, 350)
        val width = bitMatrix.width
        val height= bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for(x in 0 until width){
            for(y in 0 until height){
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        imgQr.setImageBitmap(bitmap)
    }

    private fun getIntentValue() {
        var format = SimpleDateFormat("dd-mm-yyyy hh:mm:ss")
        var format1 = SimpleDateFormat("dd-mm-yyyy")

        i = intent
        sp = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        idTransaksi = i.getIntExtra("idTransaksi", 0)
        idPelanggan = i.getStringExtra("idPelanggan").toString()
        namaPelanggan = i.getStringExtra("namaPelanggan").toString()
        alamatPelanggan = i.getStringExtra("alamatPelanggan").toString()
        nohp = i.getStringExtra("nohp").toString()
        idProduk = i.getStringExtra("idProduk").toString()
        namaProduk = i.getStringExtra("namaProduk").toString()
        hargaProduk = i.getIntExtra("hargaProduk", 0)


        val tglMasukParse = i.getStringExtra("tglMasuk").toString()
        val date1: Date = format.parse(tglMasukParse)
        format = SimpleDateFormat("dd-MMM-yy hh:mm:ss a")
        tglMasuk = format.format(date1)


        val tglKeluarParse = i.getStringExtra("tglSelesai").toString()
        val date2: Date = format1.parse(tglKeluarParse)
        tglSelesai = tglKeluarParse

        metodeBayar = i.getStringExtra("metodeBayar").toString()
        jumlah = i.getIntExtra("jumlah", 0)
        jumlahBayar = i.getIntExtra("jumlahBayar", 0)
        totalDibayar = i.getIntExtra("totalDibayar", 0)
        catatan = i.getStringExtra("catatan").toString()
        statusBayar = i.getStringExtra("statusBayar").toString()
        namaLaundry = sp.getString("namaLaundry", "").toString()
        alamatLaundry = sp.getString("alamatLaundry", "").toString()

        nota = "$namaLaundry\n $alamatLaundry\n\n===============\n" +
                "Nama: $namaPelanggan\nAlamat : $alamatPelanggan\nNo HP : $nohp\nTanggal : $tglMasuk\nEstimasi: $tglSelesai\n\n==============\n" +
                "Pesanan : $namaProduk\nHarga: $hargaProduk\nBerat(Kg) / Jumlah(Pcs) : $jumlah\nTotal: $jumlahBayar\nTotal Bayar: $totalDibayar\nStatus: $statusBayar"
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
        txtOrderTotalBayar.setText("Rp. ${totalDibayar.toString()}")
        if (statusBayar?.equals("Belum Lunas")){
            spinStatusBayar.visibility = View.VISIBLE
            layoutEtOrderJumlah.visibility = View.VISIBLE
            etOrderTotalBayar.visibility = View.VISIBLE
            txtOrderStatusBayar.visibility = View.GONE
            txtOrderTotalBayar.visibility = View.GONE
        }
    }

    private fun kirimNota() {
        val intent = Intent(Intent.ACTION_SEND)
//        intent.setType("text/plain")
       val bitmap = imgQr.drawable.toBitmap()
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            applicationContext.contentResolver, bitmap,
            "tempimage",
            null
        )
        val uri = Uri.parse(path)
        val changeTo = nohp.take(2).replace("08","+628")
        val realnumber = changeTo + nohp.takeLast(nohp.length-2)
        intent.data =
            Uri.parse("https://wa.me/$realnumber?text=$nota")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.setPackage("com.whatsapp")
        intent.putExtra(Intent.EXTRA_TEXT, nota)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
//
        intent.setType("image/jpeg")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


        try{
            startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException){
            e.printStackTrace()
            Toast.makeText(applicationContext, "Whatsapp not insalled", Toast.LENGTH_SHORT).show()

        }


    }

    private fun editOrder() {
        val totalBayar =  etOrderTotalBayar.text.toString()
        val statusBayar = spinStatusBayar.selectedItem.toString()
        val idUser = sp.getInt("id", 0).toString()
//        Toast.makeText(applicationContext, "ID Transaksi : $idTransaksi,ID User: $idUser, Total Bayar : $totalBayar, status bayar : $statusBayar", Toast.LENGTH_LONG).show()
        val loading= ProgressDialog(this)
        loading.setMessage("Mengubah data..")
        loading.show()

        AndroidNetworking.post(ApiOrder.UPDATE)
            .addBodyParameter("idTrans", idTransaksi.toString())
            .addBodyParameter("idUser", idUser)
            .addBodyParameter("totalBayar", totalBayar)
            .addBodyParameter("statusBayar", statusBayar)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(
                        applicationContext,
                        response?.getString("message"),
                        Toast.LENGTH_LONG
                    ).show()

                    if (response?.getString("message")?.contains("successfully")!!) {
                        this@ManageOrder.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_LONG)
                        .show()
                }

            })

    }


}