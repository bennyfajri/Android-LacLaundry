package com.benny.laclaundry.data.remote

import com.google.gson.annotations.SerializedName

class SignupRequest(
    @SerializedName("username")val username: String,
    @SerializedName("namaLaundry")val namaLaundry: String,
    @SerializedName("alamat")val alamatLaundry: String,
    @SerializedName("password")val password: String,
)