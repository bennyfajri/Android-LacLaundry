package com.benny.laclaundry.home.produk

data class ModelProduk(
    val idProduk: Int,
    val idUser: Int,
    val jenis: String,
    val namaProduk: String,
    val hargaProduk: Int,
)
