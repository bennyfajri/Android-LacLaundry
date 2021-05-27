package com.benny.laclaundry.data.retrofit

import com.benny.laclaundry.URL
import retrofit2.Retrofit

object ApiService {
    private val TAG = "--ApiService"

    val BASE_URL = URL.server

    fun loginApiCall() = Retrofit.Builder()
        .baseUrl(BASE_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ApiWorker.gsonConverter)
        .client(ApiWorker.client)
        .build()
        .create(ApiList::class.java)!!
}