package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.network.api.ApodApiService
import com.udacity.asteroidradar.network.api.AsteroidApiService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkService {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val asteroidApiService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }

    val apodApiService: ApodApiService by lazy {
        retrofit.create(ApodApiService::class.java)
    }
}