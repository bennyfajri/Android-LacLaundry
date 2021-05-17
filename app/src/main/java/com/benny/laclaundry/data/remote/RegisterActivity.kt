package com.benny.laclaundry.data.remote

import android.util.Log
import android.widget.Toast
import com.benny.laclaundry.data.remote.SignupRequest
import com.benny.laclaundry.data.remote.SignupResponse
import com.benny.laclaundry.data.retrofit.ApiService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.benny.laclaundry.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity: AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnRegister ->{
                if(validation()){
                    val json = JSONObject()
                    json.put("username", etUserName.text.toString())
                    json.put("namalaundry", etNamaLaundry.text.toString())
                    json.put("namauser",etNamaUser.text.toString())
                    json.put("password",etPassword.text.toString())

                    ApiService.loginApiCall().doRegister(
                        SignupRequest(
                            etUserName.text.toString(),
                            etNamaLaundry.text.toString(),
                            etNamaUser.text.toString(),
                            etPassword.text.toString()
                        )
                    ).enqueue(object : Callback<SignupResponse>{
                        override fun onResponse(
                            call: Call<SignupResponse>,
                            response: Response<SignupResponse>
                        ) {
                            Log.d("Response::::", response.body().toString())
                            val loginResponse : SignupResponse
                            loginResponse = response.body()!!
                            if(loginResponse.status){
                                Toast.makeText(applicationContext, "Berhasil Daftar akun",Toast.LENGTH_LONG).show()
                                finish()
                            }else{
                                Toast.makeText(applicationContext, response.body()!!.message,
                                Toast.LENGTH_LONG).show()
                            }

                        }

                        override fun onFailure(call: Call<SignupResponse>, t: Throwable) {

                        }
                    })
                }
            }
        }
    }

    private fun validation(): Boolean {
        var value = true

        val namaLaundry = etNamaLaundry.text.toString()
        val namaUser = etNamaUser.text.toString()
        val username = etUserName.text.toString()
        val password = etPassword.text.toString()

        if(namaLaundry.isEmpty()){
            etNamaLaundry.error = "Nama Laundry Kosong"
            etNamaLaundry.requestFocus()
            value = false
        }

        if(namaUser.isEmpty()){
            etNamaUser.error = "Nama User Kosong"
            etNamaUser.requestFocus()
            value = false
        }

        if(username.isEmpty()){
            etUserName.error = "Nama Laundry Kosong"
            etUserName.requestFocus()
            value = false
        }

        if(password.isEmpty()){
            etPassword.error = "Nama Laundry Kosong"
            etPassword.requestFocus()
            value = false
        }

        return value
    }


}