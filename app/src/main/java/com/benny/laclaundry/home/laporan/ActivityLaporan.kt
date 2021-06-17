package com.benny.laclaundry.home.laporan

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.benny.laclaundry.R
import com.benny.laclaundry.URL
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import kotlinx.android.synthetic.main.activity_laporan.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class ActivityLaporan : AppCompatActivity() {

    lateinit var sp: SharedPreferences
    var arrayList = ArrayList<ModelLaporan>()
    var total = 0
    lateinit var jenis: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        sp = getSharedPreferences("user_info", Context.MODE_PRIVATE)

        spinJenisLaporan.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val idUser = sp.getInt("id", 0)
                if (parent.getItemAtPosition(position).toString() == "Harian") {
                    jenis = parent.selectedItem.toString()
                } else if (parent.getItemAtPosition(position).toString() == "Mingguan") {
                    jenis = parent.selectedItem.toString()
                } else {
                    jenis = parent.selectedItem.toString()
                }
                val url = "${URL.server}pendapatan.php?idUser=$idUser&jenis=$jenis"
//              Toast.makeText(applicationContext, url, Toast.LENGTH_LONG).show()
                AndroidNetworking.get(url)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            arrayList.clear()
                            val jsonArray = response?.optJSONArray("result")

                            if (jsonArray?.length() == 0) {
                                Toast.makeText(
                                    applicationContext, "Data Kosong",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            for (i in 0 until jsonArray?.length()!!) {
                                val jsonObject = jsonArray?.optJSONObject(i)

                                arrayList.add(ModelLaporan(jsonObject.getInt("Total")))

                                if (jsonArray?.length() - 1 == i) {
//                            Toast.makeText(applicationContext, "$total", Toast.LENGTH_LONG).show()
                                }
                            }


                        }

                        override fun onError(anError: ANError?) {
                            Log.e("ONERROR", anError?.errorDetail?.toString()!!)
                            Toast.makeText(
                                applicationContext,
                                "Connection Failure",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        btnPilihLaporan.setOnClickListener {
            showIncome()
        }
    }

    private fun showIncome() {
        var pendapatan = arrayList[0]

        val localeID = Locale("in","ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        var total = numberFormat.format(pendapatan.Total)

        val namaLaundry = sp.getString("namaLaundry", "").toString()
        val namaUser = sp.getString("username", "").toString()
        val pesan =  "Hallo $namaUser!\ntotal pendapatan $jenis anda adalah $total"
        val jenis = spinJenisLaporan.selectedItem.toString()
//        Toast.makeText(applicationContext, "$pendapatan", Toast.LENGTH_LONG).show()

        val dialog = MaterialStyledDialog.Builder(this)
            .setTitle(namaLaundry)
            .setDescription(pesan)
            .setIcon(R.drawable.coin)
            .withIconAnimation(false)
            .setHeaderDrawable(R.drawable.header)
            .setPositiveText("tutup")
            .build()
        dialog.show()

    }
}