package com.benny.laclaundry.ui.order

data class ModelOrder(
    val idTransaksi: Int,
    val idPelanggan: Int,
    val namaPelanggan: String,
    val alamatPelanggan: String,
    val nohp: String,
    val idProduk: Int,
    val namaProduk: String,
    val tglMasuk: String,
    val tglSelesai: String,
    val metodeBayar: String,
    val jumlahBayar: Int,
    val totalDibayar: Int,
    val catatan: String,
    val statusBayar: String
)