package com.example.mobile_assignment_2.data

/**
 * Data Access Object for Location operations
 * Provides methods to interact with the location table
 */
interface LocationDao {

    /**
     * Get all locations from the database
     */
    suspend fun getAllLocations(): List<Location>

    /**
     * Search for locations by address (case-insensitive)
     */
    suspend fun searchByAddress(address: String): List<Location>

    /**
     * Get a specific location by ID
     */
    suspend fun getLocationById(id: Int): Location?

    /**
     * Insert a new location
     */
    suspend fun insertLocation(location: Location): Long

    /**
     * Update an existing location
     */
    suspend fun updateLocation(location: Location): Int

    /**
     * Delete a location by ID
     */
    suspend fun deleteLocation(id: Int): Int

    /**
     * Delete all locations (for testing/reset purposes)
     */
    suspend fun deleteAllLocations(): Int
}