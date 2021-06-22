package com.benny.laclaundry.ui.transaksi

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.benny.laclaundry.URL
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_konfirmasi_pesanan.*
import kotlinx.android.synthetic.main.fragment_transaksi.*
import kotlinx.android.synthetic.main.pelanggan_list.*
import kotlinx.android.synthetic.main.product_list.*
import org.json.JSONObject
import java.util.*

class KonfirmasiPesanan : AppCompatActivity() {
    lateinit var i: Intent
    lateinit var sp: SharedPreferences
    var tanggalSelesai = ""
    var cal = Calendar.getInstance()
    lateinit var idPelanggan: String
    lateinit var idProduk: String
    lateinit var jumlah: String
    var jumlahHarga = 0
    lateinit var etJumlahBayar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_pesanan)

        i = intent
        sp = getSharedPreferences("user_info", Context.MODE_PRIVATE)

        idPelanggan = i.getStringExtra("idPelanggan").toString()
        idProduk = i.getStringExtra("idProduk").toString()
        jumlah = i.getStringExtra("jumlah").toString()
        jumlahHarga = i.getIntExtra("hargaDibayar", 0)

        txtConfNamaPel.setText(i.getStringExtra("namaPelanggan").toString())
        txtConfNamaPr.setText(i.getStringExtra("namaProduk").toString())
        txtTotalBayar.setText(jumlahHarga.toString())

        etJumlahBayar = findViewById(R.id.etJumlahBayar)
        etJumlahBayar.setText("0")

        btnEstimasi.setOnClickListener {
            showDateDialog()
        }

        spinJenisPembayaran.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (parent.getItemAtPosition(position).toString() == "Sekarang") {
                    layoutEtJumlah.visibility = View.VISIBLE
                    txtJumlahBayar.visibility = View.VISIBLE
                    etJumlahBayar.visibility = View.VISIBLE
                } else {
                    layoutEtJumlah.visibility = View.GONE
                    txtJumlahBayar.visibility = View.GONE
                    etJumlahBayar.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        btnSubmitTransaksi.setOnClickListener {
            tambahTransaksi()
        }
    }

    private fun showDateDialog() {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dateSetListener = DatePickerDialog(
            this@KonfirmasiPesanan,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                tanggalSelesai = "$year-${month + 1}-$dayOfMonth"
                etEstimasi.setText(tanggalSelesai.toString())
            },
            year,
            month,
            day
        )
        dateSetListener.show()
    }

    private fun tambahTransaksi() {
        if (validation()) {
            val idUser = sp.getInt("id", 0).toString()
            var statusBayar = ""
            var totalDibayar = etJumlahBayar.text.toString().toInt()
            if (totalDibayar >= jumlahHarga) {
                statusBayar = "Lunas"
            } else {
                statusBayar = "Belum Lunas"
            }
            val catatan = etCatatan.text.toString()
            val metodeBayar = spinJenisPembayaran.selectedItem.toString()
//        val alamatLaundry = sp.getString("alamatLaundry", "").toString()
//        val namaLaundry = sp.getString("namaLaundry", "").toString()
//
//        val pesan = "$alamatLaundry, $namaLaundry"

//        val pesan = "$idUser , $idProduk, $idPelanggan, $tanggalSelesai, \n$statusBayar, $jumlahBayar, ${etCatatan.text.toString()},\n ${spinJenisPembayaran.selectedItem.toString()}"
//        Toast.makeText(applicationContext, pesan, Toast.LENGTH_LONG).show()
            val url = URL.server + "add_transaksi.php"
            val loading = ProgressDialog(this)
            loading.setMessage("Menyimpan data..")
            loading.show()

            AndroidNetworking.post(url)
                .addBodyParameter("idUser", idUser)
                .addBodyParameter("idProduk", idProduk)
                .addBodyParameter("idPelanggan", idPelanggan)
                .addBodyParameter("tglSelesai", tanggalSelesai)
                .addBodyParameter("statusBayar", statusBayar)
                .addBodyParameter("jumlahHarga", jumlahHarga.toString())
                .addBodyParameter("totalDibayar", totalDibayar.toString())
                .addBodyParameter("catatan", catatan)
                .addBodyParameter("metodeBayar", metodeBayar)
                .addBodyParameter("jumlah", jumlah)
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
                            this@KonfirmasiPesanan.finish()
                        }
                    }

                    override fun onError(anError: ANError?) {
                    }

                })
        }

    }

    private fun validation(): Boolean {
        var value = true

        val estimasi = etEstimasi.text.toString().trim()
        val catatan = etCatatan.text.toString().trim()

        if (estimasi.isEmpty()) {
            etEstimasi.error = "estimasi kosong"
            value = false
        }
        if (catatan.isEmpty()) {
            etCatatan.error = "catatan kosong"
            etCatatan.requestFocus()
            value = false
        }
        return value
    }
}