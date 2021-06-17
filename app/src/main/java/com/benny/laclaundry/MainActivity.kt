package com.benny.laclaundry

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import com.benny.laclaundry.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.benny.laclaundry.data.remote.LoginActivity
import com.benny.laclaundry.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = getSharedPreferences("user_info",Context.MODE_PRIVATE)

        val binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home,R.id.nav_transaksi,R.id.nav_order
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.apply {
            navBottom.setupWithNavController(navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah anda yakin ingin keluar?")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which ->
                        val edit = sp.edit()
                        edit.remove("username")
                        edit.remove("password")
                        edit.commit()

                        val intent = Intent(this@MainActivity,LoginActivity::class.java)
                        startActivity(intent)
                    })
                    .setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .show()
                sp = getSharedPreferences("user_info",Context.MODE_PRIVATE)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}