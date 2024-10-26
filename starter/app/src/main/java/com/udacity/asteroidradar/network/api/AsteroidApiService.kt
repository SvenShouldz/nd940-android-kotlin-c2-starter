package com.udacity.asteroidradar.network.api

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): AsteroidResponse
}

data class AsteroidResponse(
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<AsteroidNetwork>>
)

// This class will be used to parse the data from the network response
data class AsteroidNetwork(
    val id: Long,
    val name: String,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameter,
    @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachData>,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean
)

data class EstimatedDiameter(
    @Json(name = "kilometers") val kilometers: DiameterData
)

data class DiameterData(
    @Json(name = "estimated_diameter_max") val maxDiameter: Double
)

data class CloseApproachData(
    @Json(name = "relative_velocity") val relativeVelocity: VelocityData,
    @Json(name = "miss_distance") val missDistance: DistanceData,
    @Json(name = "close_approach_date") val closeApproachDate: String
)

data class VelocityData(
    @Json(name = "kilometers_per_second") val velocity: Double
)

data class DistanceData(
    @Json(name = "astronomical") val astronomicalDistance: Double
)
