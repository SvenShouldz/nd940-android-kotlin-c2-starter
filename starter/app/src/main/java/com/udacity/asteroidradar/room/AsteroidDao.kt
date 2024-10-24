package com.udacity.asteroidradar.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroid ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid WHERE closeApproachDate < :today")
    suspend fun deleteOldAsteroids(today: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<Asteroid>)
}