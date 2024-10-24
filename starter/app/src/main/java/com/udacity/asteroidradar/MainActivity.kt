package com.udacity.asteroidradar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.network.AsteroidCacheWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Schedule the asteroid caching worker
        scheduleAsteroidCacheWork()

        setContentView(R.layout.activity_main)
    }

    fun scheduleAsteroidCacheWork() {
        // Set constraints for the worker (Wi-Fi and charging)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) // Requires Wi-Fi
            .setRequiresCharging(true) // Requires device to be charging
            .build()

        // Schedule the work to repeat every 24 hours
        val repeatingRequest = PeriodicWorkRequestBuilder<AsteroidCacheWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        // Enqueue work
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "AsteroidCacheWork",
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
    }
}
