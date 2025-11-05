package com.example.mobile_assignment_2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_assignment_2.data.Location
import com.example.mobile_assignment_2.repository.LocationRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing location data and UI state
 */
class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    var locations by mutableStateOf<List<Location>>(emptyList())
        private set

    var searchQuery by mutableStateOf("")
        private set

    var selectedLocation by mutableStateOf<Location?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadLocations()
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        searchLocations()
    }

    fun selectLocation(location: Location) {
        selectedLocation = location
    }

    fun clearSelection() {
        selectedLocation = null
    }

    private fun loadLocations() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                locations = repository.getAllLocations()
            } catch (e: Exception) {
                errorMessage = "Failed to load locations: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun searchLocations() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                locations = repository.searchLocations(searchQuery)
            } catch (e: Exception) {
                errorMessage = "Search failed: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addLocation(address: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val newLocation = Location(
                    address = address,
                    latitude = latitude,
                    longitude = longitude
                )
                repository.insertLocation(newLocation)
                searchLocations() // Refresh the list
            } catch (e: Exception) {
                errorMessage = "Failed to add location: ${e.message}"
            }
        }
    }

    fun updateLocation(location: Location) {
        viewModelScope.launch {
            try {
                repository.updateLocation(location)
                searchLocations() // Refresh the list
            } catch (e: Exception) {
                errorMessage = "Failed to update location: ${e.message}"
            }
        }
    }

    fun deleteLocation(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteLocation(id)
                if (selectedLocation?.id == id) {
                    selectedLocation = null
                }
                searchLocations() // Refresh the list
            } catch (e: Exception) {
                errorMessage = "Failed to delete location: ${e.message}"
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}