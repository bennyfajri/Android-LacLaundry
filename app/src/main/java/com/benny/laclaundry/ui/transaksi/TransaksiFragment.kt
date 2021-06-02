package com.benny.laclaundry.ui.transaksi

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.benny.laclaundry.R
import com.benny.laclaundry.home.pelanggan.ModelPelanggan
import com.benny.laclaundry.home.produk.ModelProduk
import com.jacksonandroidnetworking.JacksonParserFactory

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


    }



}