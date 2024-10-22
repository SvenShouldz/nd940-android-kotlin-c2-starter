package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.network.AsteroidRepository
import com.udacity.asteroidradar.room.Asteroid
import com.udacity.asteroidradar.room.AsteroidDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getDatabase(application)
    private val repository = AsteroidRepository(database.asteroidDao)

    val asteroidList: LiveData<List<Asteroid>> = repository.getAsteroids()

    init {
        // Initiate viewModel with new Asteroids
        viewModelScope.launch {
            repository.refreshAsteroids(BuildConfig.NASA_API_KEY)
        }
    }
}