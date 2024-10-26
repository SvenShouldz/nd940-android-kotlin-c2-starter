package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.network.AsteroidRepository
import com.udacity.asteroidradar.network.api.ApodResponse
import com.udacity.asteroidradar.room.Asteroid
import com.udacity.asteroidradar.room.AsteroidDatabase
import kotlinx.coroutines.launch

enum class AsteroidFilter {
    SHOW_TODAY,
    SHOW_THIS_WEEK,
    SHOW_SAVED
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AsteroidDatabase.getDatabase(application)
    private val repository = AsteroidRepository(database.asteroidDao)

    val asteroidList: LiveData<List<Asteroid>> = repository.getAsteroids()

    private val _apod = MutableLiveData<ApodResponse?>()
    val apod: LiveData<ApodResponse?> get() = _apod

    private val _filter = MutableLiveData(AsteroidFilter.SHOW_THIS_WEEK)
    val filter: LiveData<AsteroidFilter> = _filter

    init {
        // Initiate viewModel with new Asteroids
        viewModelScope.launch {
            repository.refreshAsteroids(BuildConfig.NASA_API_KEY)
            _apod.value = repository.fetchImageOfTheDay(BuildConfig.NASA_API_KEY)
        }
    }

    // Get the filtered list
    val filteredAsteroids: LiveData<List<Asteroid>> = filter.switchMap { filter ->
        when (filter) {
            AsteroidFilter.SHOW_TODAY -> repository.getAsteroidsForToday()
            AsteroidFilter.SHOW_THIS_WEEK -> repository.getAsteroidsForThisWeek()
            AsteroidFilter.SHOW_SAVED -> repository.getAsteroids()
        }
    }

    // Function to set the current filter
    fun setFilter(newFilter: AsteroidFilter) {
        _filter.value = newFilter
    }

}