package com.benny.laclaundry.home.pelanggan

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import kotlinx.android.synthetic.main.activity_manage_pelanggan.*
import kotlinx.android.synthetic.main.pelanggan_list.*
import org.json.JSONObject

class ManagePelanggan : AppCompatActivity() {
    lateinit var i: Intent
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_pelanggan)

        i = intent
        sp = getSharedPreferences("user_info", Context.MODE_PRIVATE)

        if(i.hasExtra(("editmode"))){
            if(i.getStringExtra("editmode").equals("1")){
                onEditMode()
            }
        }

        btnTambahPelanggan.setOnClickListener{
            createPelanggan()
        }
        btnUpdatePelanggan.setOnClickListener {
            updatePelanggan()
        }
        btnDeletePelanggan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Hapus data ini?")
                .setPositiveButton("Hapus", DialogInterface.OnClickListener { dialog, which ->
                    deletePelanggan()
                })
                .setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .show()
        }

    }

    private fun onEditMode() {
        etIDPelanggan.visibility = View.VISIBLE
        etIDPelanggan.setText(i.getIntExtra("idPelanggan",0).toString())
        etIDPelanggan.isEnabled = false
        etNamaPelanggan.setText(i.getStringExtra("namaPelanggan"))
        etNohp.setText(i.getStringExtra("nohp"))
        etAlamat.setText(i.getStringExtra("alamat"))

        btnTambahPelanggan.visibility = View.GONE
        btnUpdatePelanggan.visibility = View.VISIBLE
        btnDeletePelanggan.visibility = View.VISIBLE
    }

    private fun createPelanggan() {
        val loading = ProgressDialog(this)
        loading.setMessage("Menambahkan data..")
        loading.show()

        AndroidNetworking.post(ApiPelanggan.CREATE)
            .addBodyParameter("idUser", sp.getInt("id", 0).toString())
            .addBodyParameter("namaPelanggan",etNamaPelanggan.text.toString())
            .addBodyParameter("nohp",etNohp.text.toString())
            .addBodyParameter("alamat",etAlamat.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()

                    if (response?.getString("message")?.contains("successfully")!!){
                        this@ManagePelanggan.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updatePelanggan() {
        val loading  = ProgressDialog(this)
        loading.setMessage("Mengubah data...")
        loading.show()

        AndroidNetworking.post(ApiPelanggan.UPDATE)
            .addBodyParameter("idPelanggan", etIDPelanggan.text.toString())
            .addBodyParameter("idUser", sp.getInt("id",0).toString())
            .addBodyParameter("namaPelanggan", etNamaPelanggan.text.toString())
            .addBodyParameter("nohp", etNohp.text.toString())
            .addBodyParameter("alamat", etAlamat.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_SHORT).show()

                    if (response?.getString("message")?.contains("successfully")!!) {
                        this@ManagePelanggan.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun deletePelanggan() {
        val loading = ProgressDialog(this)
        loading.setMessage("Menghapus data...")
        loading.show()

        AndroidNetworking.get(ApiPelanggan.DELETE + "?idPelanggan=" + etIDPelanggan.text.toString() + "&idUser=" + sp.getInt("id",0))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_SHORT).show()

                    if (response?.getString("message")?.contains("successfully")!!) {
                        this@ManagePelanggan.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }



}