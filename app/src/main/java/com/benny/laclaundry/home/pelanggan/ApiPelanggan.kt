package com.benny.laclaundry.home.pelanggan

import com.benny.laclaundry.URL

class ApiPelanggan {
    companion object{
        private val SERVER = URL.server
        val CREATE = SERVER+"add_pelanggan.php"
        val READ = SERVER+"list_pelanggan.php"
        val DELETE = SERVER+"delete_pelanggan.php"
        val UPDATE = SERVER+"edit_pelanggan.php"
    }
}