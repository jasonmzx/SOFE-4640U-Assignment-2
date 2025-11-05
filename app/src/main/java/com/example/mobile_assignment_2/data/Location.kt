package com.example.mobile_assignment_2.data

/**
 * Entity representing a location in the database
 * Contains id, address, latitude, and longitude as specified in requirements
 */
data class Location(
    val id: Int = 0,
    val address: String,
    val latitude: Double,
    val longitude: Double
)