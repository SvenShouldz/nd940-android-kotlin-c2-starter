package com.udacity.asteroidradar.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApodApiService {
    @GET("planetary/apod")
    suspend fun getImageOfTheDay(
        @Query("api_key") apiKey: String
    ): ApodResponse
}

data class ApodResponse(
    val title: String,
    val explanation: String,
    val url: String
)