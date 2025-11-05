package com.example.mobile_assignment_2.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * SQLite database helper for managing location data
 * Handles database creation, version management, and basic operations
 */
class LocationDatabase private constructor(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "locations.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_LOCATIONS = "locations"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"

        @Volatile
        private var INSTANCE: LocationDatabase? = null

        fun getDatabase(context: Context): LocationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = LocationDatabase(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSQL = """
            CREATE TABLE $TABLE_LOCATIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ADDRESS TEXT NOT NULL,
                $COLUMN_LATITUDE REAL NOT NULL,
                $COLUMN_LONGITUDE REAL NOT NULL
            )
        """.trimIndent()

        db?.execSQL(createTableSQL)

        // Populate with initial GTA locations
        populateInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATIONS")
        onCreate(db)
    }

    /**
     * Populate database with 100 Greater Toronto Area locations
     */
    private fun populateInitialData(db: SQLiteDatabase?) {
        val gtaLocations = listOf(
            // Downtown Toronto
            Location(0, "CN Tower, Toronto, ON", 43.6426, -79.3871),
            Location(0, "Union Station, Toronto, ON", 43.6452, -79.3806),
            Location(0, "Toronto City Hall, Toronto, ON", 43.6534, -79.3839),
            Location(0, "Harbourfront Centre, Toronto, ON", 43.6387, -79.3816),
            Location(0, "St. Lawrence Market, Toronto, ON", 43.6488, -79.3716),
            Location(0, "Eaton Centre, Toronto, ON", 43.6544, -79.3807),
            Location(0, "Financial District, Toronto, ON", 43.6481, -79.3817),
            Location(0, "Distillery District, Toronto, ON", 43.6503, -79.3594),
            Location(0, "Entertainment District, Toronto, ON", 43.6425, -79.3892),
            Location(0, "Queen Street West, Toronto, ON", 43.6504, -79.3928),

            // North York
            Location(0, "North York Centre, Toronto, ON", 43.7670, -79.4140),
            Location(0, "Mel Lastman Square, Toronto, ON", 43.7684, -79.4127),
            Location(0, "Finch Station, Toronto, ON", 43.7806, -79.4146),
            Location(0, "York University, Toronto, ON", 43.7735, -79.5019),
            Location(0, "Sheppard-Yonge, Toronto, ON", 43.7614, -79.4107),
            Location(0, "Don Mills Centre, Toronto, ON", 43.7754, -79.3467),
            Location(0, "Fairview Mall, Toronto, ON", 43.7781, -79.3450),
            Location(0, "Bayview Village, Toronto, ON", 43.7671, -79.3890),
            Location(0, "Willowdale, Toronto, ON", 43.7731, -79.4056),
            Location(0, "Thornhill, Toronto, ON", 43.8161, -79.4291),

            // Scarborough
            Location(0, "Scarborough Town Centre, Toronto, ON", 43.7764, -79.2581),
            Location(0, "Kennedy Station, Toronto, ON", 43.7326, -79.2649),
            Location(0, "Agincourt Mall, Toronto, ON", 43.7864, -79.2686),
            Location(0, "Cedarbrae Mall, Toronto, ON", 43.7587, -79.2370),
            Location(0, "Scarborough Bluffs, Toronto, ON", 43.7051, -79.2390),
            Location(0, "Malvern Town Centre, Toronto, ON", 43.8067, -79.2267),
            Location(0, "Morningside Heights, Toronto, ON", 43.8089, -79.1867),
            Location(0, "Rouge Hill GO Station, Toronto, ON", 43.7983, -79.1383),
            Location(0, "University of Toronto Scarborough, Toronto, ON", 43.7841, -79.1864),
            Location(0, "Guildwood GO Station, Toronto, ON", 43.7464, -79.1967),

            // Etobicoke
            Location(0, "Sherway Gardens, Toronto, ON", 43.6117, -79.5579),
            Location(0, "Islington Station, Toronto, ON", 43.6285, -79.5244),
            Location(0, "Kipling Station, Toronto, ON", 43.6367, -79.5348),
            Location(0, "Humber College, Toronto, ON", 43.7283, -79.6076),
            Location(0, "Pearson Airport, Toronto, ON", 43.6777, -79.6248),
            Location(0, "Woodbine Centre, Toronto, ON", 43.6206, -79.5434),
            Location(0, "Long Branch GO Station, Toronto, ON", 43.5883, -79.5432),
            Location(0, "Mimico GO Station, Toronto, ON", 43.6067, -79.5017),
            Location(0, "Royal York Station, Toronto, ON", 43.6485, -79.5317),
            Location(0, "Cloverdale Mall, Toronto, ON", 43.6569, -79.5658),

            // Mississauga
            Location(0, "Square One Shopping Centre, Mississauga, ON", 43.5934, -79.6441),
            Location(0, "Mississauga City Centre, Mississauga, ON", 43.5890, -79.6441),
            Location(0, "Port Credit GO Station, Mississauga, ON", 43.5518, -79.5907),
            Location(0, "Cooksville GO Station, Mississauga, ON", 43.5797, -79.6514),
            Location(0, "Meadowvale Town Centre, Mississauga, ON", 43.5898, -79.7342),
            Location(0, "Heartland Town Centre, Mississauga, ON", 43.6275, -79.7342),
            Location(0, "University of Toronto Mississauga, Mississauga, ON", 43.5482, -79.6618),
            Location(0, "Erin Mills Town Centre, Mississauga, ON", 43.5570, -79.7009),
            Location(0, "Dixie GO Station, Mississauga, ON", 43.6106, -79.5707),
            Location(0, "Clarkson GO Station, Mississauga, ON", 43.5251, -79.6143),

            // Brampton
            Location(0, "Bramalea City Centre, Brampton, ON", 43.7064, -79.7645),
            Location(0, "Downtown Brampton, Brampton, ON", 43.6837, -79.7624),
            Location(0, "Brampton GO Station, Brampton, ON", 43.6837, -79.7712),
            Location(0, "Sheridan College Brampton, Brampton, ON", 43.7341, -79.7623),
            Location(0, "Trinity Commons, Brampton, ON", 43.6456, -79.7481),
            Location(0, "Chinguacousy Park, Brampton, ON", 43.7308, -79.7623),
            Location(0, "Gage Park, Brampton, ON", 43.6932, -79.7481),
            Location(0, "Mount Pleasant GO Station, Brampton, ON", 43.7064, -79.8456),
            Location(0, "Malton GO Station, Brampton, ON", 43.6767, -79.6234),
            Location(0, "Bramalea GO Station, Brampton, ON", 43.7064, -79.7645),

            // Markham
            Location(0, "Markham Town Centre, Markham, ON", 43.8561, -79.3370),
            Location(0, "Markville Shopping Centre, Markham, ON", 43.8267, -79.3189),
            Location(0, "Unionville GO Station, Markham, ON", 43.8661, -79.3178),
            Location(0, "Centennial GO Station, Markham, ON", 43.8389, -79.2589),
            Location(0, "Main Street Unionville, Markham, ON", 43.8661, -79.3178),
            Location(0, "York University Markham, Markham, ON", 43.8461, -79.3370),
            Location(0, "Pacific Mall, Markham, ON", 43.8267, -79.3189),
            Location(0, "First Markham Place, Markham, ON", 43.8561, -79.3089),
            Location(0, "Thornlea Secondary School, Markham, ON", 43.8461, -79.3589),
            Location(0, "Mount Joy GO Station, Markham, ON", 43.8661, -79.2678),

            // Ajax
            Location(0, "Ajax GO Station, Ajax, ON", 43.8506, -79.0367),
            Location(0, "Ajax Community Centre, Ajax, ON", 43.8506, -79.0267),
            Location(0, "Pickering Town Centre, Ajax, ON", 43.8367, -79.0867),
            Location(0, "Durham College, Ajax, ON", 43.8456, -79.0456),
            Location(0, "Ajax Waterfront Park, Ajax, ON", 43.8256, -79.0167),
            Location(0, "Greenwood Conservation Area, Ajax, ON", 43.8656, -79.0567),
            Location(0, "Ajax Public Library, Ajax, ON", 43.8506, -79.0367),
            Location(0, "South Ajax Recreation Centre, Ajax, ON", 43.8306, -79.0267),
            Location(0, "Ajax High School, Ajax, ON", 43.8456, -79.0456),
            Location(0, "Duffins Creek, Ajax, ON", 43.8406, -79.0167),

            // Pickering
            Location(0, "Pickering GO Station, Pickering, ON", 43.8367, -79.0867),
            Location(0, "Pickering Recreation Complex, Pickering, ON", 43.8267, -79.0767),
            Location(0, "Seaton Community Centre, Pickering, ON", 43.8567, -79.0667),
            Location(0, "Pickering Nuclear Station, Pickering, ON", 43.8106, -79.0567),
            Location(0, "Rouge Park, Pickering, ON", 43.8206, -79.0467),
            Location(0, "Frenchman's Bay, Pickering, ON", 43.8167, -79.0767),
            Location(0, "Liverpool GO Station, Pickering, ON", 43.8467, -79.0567),
            Location(0, "Dunbarton High School, Pickering, ON", 43.8367, -79.0667),
            Location(0, "Altona Forest, Pickering, ON", 43.8467, -79.0267),
            Location(0, "Pickering Beach, Pickering, ON", 43.8067, -79.0767),

            // Oshawa
            Location(0, "Oshawa GO Station, Oshawa, ON", 43.8978, -78.8639),
            Location(0, "Oshawa Centre, Oshawa, ON", 43.9178, -78.8539),
            Location(0, "University of Ontario Institute of Technology, Oshawa, ON", 43.9456, -78.8967),
            Location(0, "Oshawa City Hall, Oshawa, ON", 43.8978, -78.8639),
            Location(0, "General Motors Centre, Oshawa, ON", 43.8978, -78.8439),
            Location(0, "Lakeview Park, Oshawa, ON", 43.8778, -78.8639),
            Location(0, "Oshawa Creek, Oshawa, ON", 43.8878, -78.8539),
            Location(0, "Whitby GO Station, Oshawa, ON", 43.8767, -78.9439),
            Location(0, "Durham College Oshawa, Oshawa, ON", 43.9456, -78.8967),
            Location(0, "Oshawa Executive Airport, Oshawa, ON", 43.9278, -78.8939),

            // Richmond Hill
            Location(0, "Richmond Hill Centre, Richmond Hill, ON", 43.8761, -79.4370),
            Location(0, "Richmond Hill GO Station, Richmond Hill, ON", 43.8761, -79.4370),
            Location(0, "Hillcrest Mall, Richmond Hill, ON", 43.8561, -79.4270),
            Location(0, "York University Richmond Hill, Richmond Hill, ON", 43.8761, -79.4170),
            Location(0, "Mill Pond Park, Richmond Hill, ON", 43.8661, -79.4270),
            Location(0, "Richmond Green Sports Centre, Richmond Hill, ON", 43.8861, -79.4370),
            Location(0, "Oak Ridges Community Centre, Richmond Hill, ON", 43.9061, -79.4470),
            Location(0, "Seneca College King City, Richmond Hill, ON", 43.9261, -79.5070),
            Location(0, "David Dunlap Observatory, Richmond Hill, ON", 43.8961, -79.4170),
            Location(0, "Richmond Hill Public Library, Richmond Hill, ON", 43.8761, -79.4370)
        )

        db?.let { database ->
            gtaLocations.forEach { location ->
                val insertSQL = """
                    INSERT INTO $TABLE_LOCATIONS ($COLUMN_ADDRESS, $COLUMN_LATITUDE, $COLUMN_LONGITUDE)
                    VALUES (?, ?, ?)
                """.trimIndent()

                database.execSQL(insertSQL, arrayOf(location.address, location.latitude, location.longitude))
            }
        }
    }

    fun getDao(): LocationDao = LocationDaoImpl(this)
}