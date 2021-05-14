package com.benny.laclaundry.data.remote

import RegisterActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.benny.laclaundry.MainActivity
import com.benny.laclaundry.R
import com.benny.laclaundry.data.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var sp: SharedPreferences
    lateinit var username: String
    lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        sp = getSharedPreferences("user_info",Context.MODE_PRIVATE)
        get_user()
        if(sp.contains("username")){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener(this)
        textviewRegister.setOnClickListener(this)
    }

    private fun get_user() {
        sp = getSharedPreferences("user_info",Context.MODE_PRIVATE)
        val usr = sp.getString("username",null)
        val pwd = sp.getString("password",null)


    }

    override fun onClick(v: View) {
        when(v?.id){
            R.id.textviewRegister -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            R.id.btnLogin -> {
                if (validaion()) {
                    val json = JSONObject()
                    json.put("username", etUserName.text.toString())
                    json.put("password", etPassword.text.toString())

                    ApiService.loginApiCall().doLogin(
                        SigninRequest(
                            etUserName.text.toString(), etPassword.text.toString()
                        )
                    ).enqueue(object : Callback<SigninResponse> {
                        override fun onResponse(
                            call: Call<SigninResponse>,
                            response: Response<SigninResponse>
                        ) {
                            Log.d("Response::::", response.body().toString())
                            if (response.body()!!.status) {
                                sp = getSharedPreferences("user_info",Context.MODE_PRIVATE)
                                if(!sp.contains("username")){
                                    remember_user(etUserName.text.toString(), etPassword.text.toString())
                                }
                                finish()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("id", response.body()!!.data.id)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    response.body()!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                        }

                    })
                }
            }
        }
    }

    private fun remember_user(username: String, password: String){
        sp = getSharedPreferences("user_info", Context.MODE_PRIVATE)

        val editor = sp.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.commit()
    }

    private fun validaion(): Boolean {
        var value = true

        val password = etPassword.text.toString().trim()
        val username = etUserName.text.toString().trim()

        if (password.isEmpty()) {
            etPassword.error = "Password required"
            etPassword.requestFocus()
            value = false
        }

        if (username.isEmpty()) {
            etUserName.error = "Email required"
            etPassword.requestFocus()
            value = false
        }

        return value;
    }

}