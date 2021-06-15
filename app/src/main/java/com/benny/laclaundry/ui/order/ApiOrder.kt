package com.benny.laclaundry.ui.order

import com.benny.laclaundry.URL

class ApiOrder {
    companion object{
        private val SERVER = URL.server
        val READ = SERVER+"list_order.php"
        val UPDATE = SERVER+"edit_order.php"
        val DELETE = SERVER+"delete_order.php"
    }
}