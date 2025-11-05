package com.example.mobile_assignment_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_assignment_2.data.Location
import com.example.mobile_assignment_2.data.LocationDatabase
import com.example.mobile_assignment_2.repository.LocationRepository
import com.example.mobile_assignment_2.ui.MapScreen
import com.example.mobile_assignment_2.ui.theme.Mobile_Assignment_2Theme
import com.example.mobile_assignment_2.viewmodel.LocationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mobile_Assignment_2Theme {
                val database = LocationDatabase.getDatabase(this)
                val repository = LocationRepository(database.getDao())
                val viewModel = remember { LocationViewModel(repository) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SpotFinderApp(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotFinderApp(
    viewModel: LocationViewModel,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingLocation by remember { mutableStateOf<Location?>(null) }
    var showMap by remember { mutableStateOf(false) }
    var mapLocation by remember { mutableStateOf<Location?>(null) }

    if (showMap) {
        MapScreen(
            location = mapLocation,
            allLocations = viewModel.locations,
            onBackPressed = {
                showMap = false
                mapLocation = null
            },
            modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "SpotFinder - GTA Locations",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SearchBar(
            query = viewModel.searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Found ${viewModel.locations.size} locations",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        mapLocation = null
                        showMap = true
                    }
                ) {
                    Icon(Icons.Default.Place, contentDescription = "View All on Map")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Map")
                }

                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Location")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.locations) { location ->
                    LocationCard(
                        location = location,
                        onLocationClick = { viewModel.selectLocation(location) },
                        onMapClick = {
                            mapLocation = location
                            showMap = true
                        },
                        onEditClick = {
                            editingLocation = location
                            showEditDialog = true
                        },
                        onDeleteClick = { viewModel.deleteLocation(location.id) }
                    )
                }
            }
        }
    }

    viewModel.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Show error (in a real app, you'd show a Snackbar)
            viewModel.clearError()
        }
    }

    if (showAddDialog) {
        AddLocationDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { address, lat, lng ->
                viewModel.addLocation(address, lat, lng)
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && editingLocation != null) {
        EditLocationDialog(
            location = editingLocation!!,
            onDismiss = {
                showEditDialog = false
                editingLocation = null
            },
            onUpdate = { updatedLocation ->
                viewModel.updateLocation(updatedLocation)
                showEditDialog = false
                editingLocation = null
            }
        )
    }

    viewModel.selectedLocation?.let { location ->
        LocationDetailsDialog(
            location = location,
            onDismiss = { viewModel.clearSelection() }
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search addresses...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        modifier = modifier
    )
}

@Composable
fun LocationCard(
    location: Location,
    onLocationClick: () -> Unit,
    onMapClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onLocationClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = location.address,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Lat: ${String.format("%.6f", location.latitude)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Lng: ${String.format("%.6f", location.longitude)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    IconButton(onClick = onMapClick) {
                        Icon(Icons.Default.LocationOn, contentDescription = "View on Map")
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun LocationDetailsDialog(
    location: Location,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Location Details") },
        text = {
            Column {
                Text("Address: ${location.address}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Latitude: ${location.latitude}")
                Text("Longitude: ${location.longitude}")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Note: Google Maps integration available with API key setup")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun AddLocationDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Double, Double) -> Unit
) {
    var address by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Location") },
        text = {
            Column {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val lat = latitude.toDoubleOrNull()
                    val lng = longitude.toDoubleOrNull()
                    if (address.isNotBlank() && lat != null && lng != null) {
                        onAdd(address, lat, lng)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditLocationDialog(
    location: Location,
    onDismiss: () -> Unit,
    onUpdate: (Location) -> Unit
) {
    var address by remember { mutableStateOf(location.address) }
    var latitude by remember { mutableStateOf(location.latitude.toString()) }
    var longitude by remember { mutableStateOf(location.longitude.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Location") },
        text = {
            Column {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val lat = latitude.toDoubleOrNull()
                    val lng = longitude.toDoubleOrNull()
                    if (address.isNotBlank() && lat != null && lng != null) {
                        onUpdate(location.copy(address = address, latitude = lat, longitude = lng))
                    }
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SpotFinderPreview() {
    Mobile_Assignment_2Theme {
        // Preview would need mock data
    }
}