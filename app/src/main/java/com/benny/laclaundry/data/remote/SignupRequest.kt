package com.benny.laclaundry.data.remote

import com.google.gson.annotations.SerializedName

class SignupRequest(
    @SerializedName("username")val username: String,
    @SerializedName("nama_user")val nama_user: String,
    @SerializedName("nama_laundry")val nama_laundry: String,
)