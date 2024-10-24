package com.udacity.asteroidradar.network

import android.util.Log
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.network.api.ApodResponse
import com.udacity.asteroidradar.room.Asteroid
import com.udacity.asteroidradar.room.AsteroidDao
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(private val asteroidDao: AsteroidDao) {

    suspend fun fetchImageOfTheDay(apiKey: String): ApodResponse? {
        return try {
            val response = NetworkService.apodApiService.getImageOfTheDay(apiKey)
            response
        } catch (e: Exception) {
            Log.e("ERROR", "Failed to fetch APOD: ${e.message}")
            null
        }
    }

    suspend fun refreshAsteroids(apiKey: String) {
        try {
            // Fetch the response from the API
            val response = NetworkService.asteroidApiService.getAsteroids(
                startDate = getTodayDate(),
                endDate = getEndDate(),
                apiKey = apiKey
            )

            // List to hold parsed Asteroids
            val asteroids = mutableListOf<Asteroid>()

            // Loop through the nearEarthObjects
            response.nearEarthObjects.forEach { (date, asteroidList) ->
                asteroidList.forEach { networkAsteroid ->
                    // Map the network response directly to the Asteroid entity
                    val asteroid = Asteroid(
                        id = networkAsteroid.id,
                        codename = networkAsteroid.name,
                        closeApproachDate = networkAsteroid.closeApproachData[0].closeApproachDate,
                        absoluteMagnitude = networkAsteroid.absoluteMagnitude,
                        estimatedDiameter = networkAsteroid.estimatedDiameter.kilometers.maxDiameter,
                        relativeVelocity = networkAsteroid.closeApproachData[0].relativeVelocity.velocity,
                        distanceFromEarth = networkAsteroid.closeApproachData[0].missDistance.astronomicalDistance,
                        isPotentiallyHazardous = networkAsteroid.isPotentiallyHazardous
                    )
                    asteroids.add(asteroid)
                }
            }

            // Insert the parsed Asteroids directly into Room database
            asteroidDao.insertAll(asteroids)

        } catch (e: Exception) {
            Log.e("ERROR", "Failed to fetch Asteroids: ${e.message}")
        }
    }

    fun getAsteroids(): LiveData<List<Asteroid>> {
        return asteroidDao.getAsteroids()
    }

    // Helper functions to get start and end dates
    fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = Calendar.getInstance().time
        return dateFormat.format(today)
    }

    fun getEndDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, DEFAULT_END_DATE_DAYS)
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
