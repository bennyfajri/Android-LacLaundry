package com.benny.laclaundry.data.remote

import com.google.gson.annotations.SerializedName

class SigninRequest(
    @SerializedName("username") var username : String,
    @SerializedName("password") var password : String
)