package com.benny.laclaundry.data.retrofit

import retrofit2.Retrofit

object ApiService {
    private val TAG = "--ApiService"

    private const val BASE_URL = "http://192.168.42.178/ServerLaundry/"

    fun loginApiCall() = Retrofit.Builder()
        .baseUrl(BASE_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ApiWorker.gsonConverter)
        .client(ApiWorker.client)
        .build()
        .create(ApiList::class.java)!!
}