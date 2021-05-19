package com.benny.laclaundry.home.produk

class ApiEndPoint {
    companion object{
        private val SERVER = "http://192.168.42.236/serverlaundry/"
        val CREATE = SERVER+"add_produk.php"
        val READ_KILOAN = SERVER+"list_produk_kiloan.php"
        val READ_SATUAN = SERVER+"list_produk_satuan.php"
        val DELETE = SERVER+"delete_produk.php"
        val UPDATE = SERVER+"edit_produk.php"
    }
}