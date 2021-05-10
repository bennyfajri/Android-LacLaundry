package com.benny.laclaundry.data.retrofit

import com.benny.laclaundry.data.remote.SigninRequest
import com.benny.laclaundry.data.remote.SigninResponse
import com.benny.laclaundry.data.remote.SignupRequest
import com.benny.laclaundry.data.remote.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiList {
    //TODO : Register User
    @POST("register.php")
    fun doRegister(
        @Body signupRequest: SignupRequest
    ): Call<SignupResponse> // body data

    //TODO : Login User
    @POST("login.php")
    fun doLogin(
        @Body signinRequest: SigninRequest
    ): Call<SigninResponse> // body data
}