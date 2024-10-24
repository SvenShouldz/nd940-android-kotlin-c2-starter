package com.udacity.asteroidradar.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.room.AsteroidDatabase

class AsteroidCacheWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val repository = AsteroidRepository(AsteroidDatabase.getDatabase(applicationContext).asteroidDao)
        return try {
            // Delete old asteroids
            repository.deleteOldAsteroids()
            // Refresh asteroids
            repository.refreshAsteroids(BuildConfig.NASA_API_KEY)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}