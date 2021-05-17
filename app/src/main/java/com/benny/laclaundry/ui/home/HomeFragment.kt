package com.benny.laclaundry.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.benny.laclaundry.R
import com.benny.laclaundry.home.laporan.ActivityLaporan
import com.benny.laclaundry.home.pelanggan.ListPelanggan
import com.benny.laclaundry.home.produk.ListProduk
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home){
    var userID : String = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.activity
            ?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val namaLaundry: String? = preferences?.getString("namaLaundry", null)
//        val Item = activity?.intent?.extras!!.getInt("id",0).toString()
        txtNamaLaundry.setText(namaLaundry)
        btnProduk.setOnClickListener { v ->
            val intent = Intent(context, ListProduk::class.java)
            startActivity(intent)
        }
        btnPelanggan.setOnClickListener { v ->
            val intent = Intent(context, ListPelanggan::class.java)
            startActivity(intent)
        }
        btnLaporan.setOnClickListener { v->
            val intent = Intent(context, ActivityLaporan::class.java)
            startActivity(intent)
        }
    }

}