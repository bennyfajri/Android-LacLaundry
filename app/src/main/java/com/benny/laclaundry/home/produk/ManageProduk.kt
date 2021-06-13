package com.benny.laclaundry.home.produk

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
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import kotlinx.android.synthetic.main.activity_manage_produk.*
import org.json.JSONObject

class ManageProduk : AppCompatActivity() {
    lateinit var i: Intent
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_produk)

        i = intent
        sp = getSharedPreferences("user_info", Context.MODE_PRIVATE)

        if(i.hasExtra("editmode")){
            if(i.getStringExtra("editmode").equals("1")){
                onEditMode()
            }
        }
        if(i.hasExtra("jenisProduk")){
            if(i.getStringExtra("jenisProduk").equals("Kiloan")){
                spinJenisProduk.setSelection(1)
            }
        }

        btnTambahProduk.setOnClickListener {
            createProduk()
        }
        btnUpdateProduk.setOnClickListener {
            updateProduk()
        }
        btnDeleteProduk.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus?")
                .setPositiveButton("Hapus", DialogInterface.OnClickListener{ dialog, which ->
                    deleteProduk()
                })
                .setNegativeButton("Batal", DialogInterface.OnClickListener{ dialog, which ->
                    dialog.dismiss()
                })
                .show()
        }
    }

    private fun onEditMode() {
        etidProduk.visibility = View.VISIBLE
        etidProduk.setText(i.getIntExtra("idProduk",0).toString())
        etidProduk.isEnabled = false
        etNamaProduk.setText(i.getStringExtra("namaProduk"))
        etHargaProduk.setText(i.getIntExtra("hargaProduk",0).toString())
        txtManageProduk.setText("Ubah Produk")

        btnTambahProduk.visibility = View.GONE
        btnUpdateProduk.visibility = View.VISIBLE
        btnDeleteProduk.visibility = View.VISIBLE
    }

    private fun createProduk() {
        val loading = ProgressDialog(this)
        loading.setMessage("Menambahkan data...")
        loading.show()

        AndroidNetworking.post(ApiProduk.CREATE)
            .addBodyParameter("idUser", sp.getInt("id", 0 ).toString())
            .addBodyParameter("jenis", spinJenisProduk.selectedItem.toString())
            .addBodyParameter("namaProduk", etNamaProduk.text.toString())
            .addBodyParameter("hargaProduk", etHargaProduk.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()

                    if(response?.getString("message")?.contains("successfully")!!){
                        this@ManageProduk.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun updateProduk() {
        val loading = ProgressDialog(this)
        loading.setMessage("Mengubah data...")
        loading.show()

        AndroidNetworking.post(ApiProduk.UPDATE)
            .addBodyParameter("idProduk", etidProduk.text.toString())
            .addBodyParameter("idUser", sp.getInt("id", 0 ).toString())
            .addBodyParameter("jenis", spinJenisProduk.selectedItem.toString())
            .addBodyParameter("namaProduk", etNamaProduk.text.toString())
            .addBodyParameter("hargaProduk", etHargaProduk.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()

                    if(response?.getString("message")?.contains("successfully")!!){
                        this@ManageProduk.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun deleteProduk() {
        val loading = ProgressDialog(this)
        loading.setMessage("Menghapus data...")
        loading.show()

        AndroidNetworking.get(ApiProduk.DELETE + "?idProduk=" + etidProduk.text.toString() + "&idUser=" + sp.getInt("id",0))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()

                    if(response?.getString("message")?.contains("successfully")!!){
                        this@ManageProduk.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail.toString()!!)
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_LONG).show()
                }

            })
    }

}