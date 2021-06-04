package com.benny.laclaundry.home.produk

import com.benny.laclaundry.URL

class ApiProduk {
    companion object{
        private val SERVER = URL.server
        val CREATE = SERVER+"add_produk.php"
        val READ_KILOAN = SERVER+"list_produk_kiloan.php"
        val READ_SATUAN = SERVER+"list_produk_satuan.php"
        val READ_SEMUA = SERVER+"list_produk.php"
        val DELETE = SERVER+"delete_produk.php"
        val UPDATE = SERVER+"edit_produk.php"
    }
}