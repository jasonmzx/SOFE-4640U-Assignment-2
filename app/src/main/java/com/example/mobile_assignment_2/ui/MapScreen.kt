package com.example.mobile_assignment_2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_assignment_2.data.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

/**
 * Screen displaying a Google Map with location markers
 * Shows selected location or all locations if none selected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    location: Location?,
    allLocations: List<Location>,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Set initial camera position
    val initialPosition = if (location != null) {
        LatLng(location.latitude, location.longitude)
    } else {
        // Center on Toronto if no specific location
        LatLng(43.6532, -79.3832)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            initialPosition,
            if (location != null) 15f else 10f
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = if (location != null) "Location: ${location.address}" else "All GTA Locations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        // Check if Google Maps API key is configured
        val apiKey = try {
            context.getString(context.resources.getIdentifier("google_maps_key", "string", context.packageName))
        } catch (e: Exception) {
            "YOUR_API_KEY_HERE"
        }

        if (apiKey == "YOUR_API_KEY_HERE" || apiKey.isBlank()) {
            // Show instructions if API key not configured
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Google Maps API Setup Required",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "To view locations on the map, please:",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. Get a Google Maps API key\n" +
                                    "2. Replace YOUR_API_KEY_HERE in strings.xml\n" +
                                    "3. Enable Maps SDK for Android",
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (location != null) {
                            Text(
                                text = "Selected Location:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(text = location.address)
                            Text(text = "Lat: ${String.format("%.6f", location.latitude)}")
                            Text(text = "Lng: ${String.format("%.6f", location.longitude)}")
                        }
                    }
                }
            }
        } else {
            // Show Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = false // Would need location permission handling
                )
            ) {
                // Add markers for locations
                val locationsToShow = if (location != null) listOf(location) else allLocations

                locationsToShow.forEach { loc ->
                    Marker(
                        state = MarkerState(position = LatLng(loc.latitude, loc.longitude)),
                        title = loc.address,
                        snippet = "Lat: ${String.format("%.6f", loc.latitude)}, Lng: ${String.format("%.6f", loc.longitude)}"
                    )
                }
            }
        }
    }

    // Move camera to selected location when it changes
    LaunchedEffect(location) {
        if (location != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    15f
                )
            )
        }
    }
}