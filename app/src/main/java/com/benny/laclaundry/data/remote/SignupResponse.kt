package com.benny.laclaundry.data.remote

data class SignupResponse(
    val status: Boolean,
    val message: String,
    val data: Laundry
)