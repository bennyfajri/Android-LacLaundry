package com.benny.laclaundry.data.remote

import com.beust.klaxon.Klaxon

val klaxon = Klaxon()
data class SigninResponse(
    val status: Boolean,
    val message:String,
    val data: Laundry
){
//    public fun toJson() = klaxon.toJsonString(this)
//
//    companion object {
//        public fun fromJson(json: String) = klaxon.parse<SigninResponse>(json)
//    }
}