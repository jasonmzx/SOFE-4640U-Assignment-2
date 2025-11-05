package com.example.mobile_assignment_2.repository

import com.example.mobile_assignment_2.data.Location
import com.example.mobile_assignment_2.data.LocationDao

/**
 * Repository class to abstract data source and provide clean API for ViewModels
 */
class LocationRepository(private val locationDao: LocationDao) {

    suspend fun getAllLocations(): List<Location> {
        return locationDao.getAllLocations()
    }

    suspend fun searchLocations(query: String): List<Location> {
        return if (query.isBlank()) {
            getAllLocations()
        } else {
            locationDao.searchByAddress(query.trim())
        }
    }

    suspend fun getLocationById(id: Int): Location? {
        return locationDao.getLocationById(id)
    }

    suspend fun insertLocation(location: Location): Long {
        return locationDao.insertLocation(location)
    }

    suspend fun updateLocation(location: Location): Int {
        return locationDao.updateLocation(location)
    }

    suspend fun deleteLocation(id: Int): Int {
        return locationDao.deleteLocation(id)
    }
}