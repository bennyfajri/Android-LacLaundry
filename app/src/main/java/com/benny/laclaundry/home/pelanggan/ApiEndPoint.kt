package com.benny.laclaundry.home.pelanggan

class ApiEndPoint {
    companion object{
        private val SERVER = "http://192.168.42.236/serverlaundry/"
        val CREATE = SERVER+"add_pelanggan.php"
        val READ = SERVER+"list_pelanggan.php"
        val DELETE = SERVER+"delete_pelanggan.php"
        val UPDATE = SERVER+"edit_pelanggan.php"
    }
}