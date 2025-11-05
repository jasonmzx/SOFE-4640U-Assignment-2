package com.example.mobile_assignment_2.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of LocationDao using SQLite operations
 */
class LocationDaoImpl(private val dbHelper: LocationDatabase) : LocationDao {

    private companion object {
        const val TABLE_LOCATIONS = "locations"
        const val COLUMN_ID = "id"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
    }

    override suspend fun getAllLocations(): List<Location> = withContext(Dispatchers.IO) {
        val locations = mutableListOf<Location>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(TABLE_LOCATIONS, null, null, null, null, null, "$COLUMN_ADDRESS ASC")

        cursor.use {
            while (it.moveToNext()) {
                locations.add(
                    Location(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                        address = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        latitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        longitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                    )
                )
            }
        }
        locations
    }

    override suspend fun searchByAddress(address: String): List<Location> = withContext(Dispatchers.IO) {
        val locations = mutableListOf<Location>()
        val db = dbHelper.readableDatabase
        val selection = "$COLUMN_ADDRESS LIKE ?"
        val selectionArgs = arrayOf("%$address%")
        val cursor = db.query(TABLE_LOCATIONS, null, selection, selectionArgs, null, null, "$COLUMN_ADDRESS ASC")

        cursor.use {
            while (it.moveToNext()) {
                locations.add(
                    Location(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                        address = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        latitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        longitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                    )
                )
            }
        }
        locations
    }

    override suspend fun getLocationById(id: Int): Location? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_LOCATIONS, null, selection, selectionArgs, null, null, null)

        cursor.use {
            if (it.moveToFirst()) {
                Location(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    address = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    latitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                    longitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                )
            } else null
        }
    }

    override suspend fun insertLocation(location: Location): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, location.address)
            put(COLUMN_LATITUDE, location.latitude)
            put(COLUMN_LONGITUDE, location.longitude)
        }
        db.insert(TABLE_LOCATIONS, null, values)
    }

    override suspend fun updateLocation(location: Location): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, location.address)
            put(COLUMN_LATITUDE, location.latitude)
            put(COLUMN_LONGITUDE, location.longitude)
        }
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(location.id.toString())
        db.update(TABLE_LOCATIONS, values, selection, selectionArgs)
    }

    override suspend fun deleteLocation(id: Int): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        db.delete(TABLE_LOCATIONS, selection, selectionArgs)
    }

    override suspend fun deleteAllLocations(): Int = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(TABLE_LOCATIONS, null, null)
    }
}