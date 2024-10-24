package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.network.AsteroidRepository
import com.udacity.asteroidradar.network.api.ApodResponse
import com.udacity.asteroidradar.room.Asteroid
import com.udacity.asteroidradar.room.AsteroidDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getDatabase(application)
    private val repository = AsteroidRepository(database.asteroidDao)

    val asteroidList: LiveData<List<Asteroid>> = repository.getAsteroids()

    private val _apod = MutableLiveData<ApodResponse?>()
    val apod: LiveData<ApodResponse?> get() = _apod

    init {
        // Initiate viewModel with new Asteroids
        viewModelScope.launch {
            repository.refreshAsteroids(BuildConfig.NASA_API_KEY)
            _apod.value = repository.fetchImageOfTheDay(BuildConfig.NASA_API_KEY)
        }
    }

}