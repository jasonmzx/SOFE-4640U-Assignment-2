# SpotFinder - GTA Locations App

A comprehensive Android application for managing and viewing Greater Toronto Area (GTA) locations with map integration.

## 📱 Features

- **Location Database**: SQLite database with 100+ pre-populated GTA locations
- **Search Functionality**: Real-time search through addresses
- **CRUD Operations**: Add, edit, and delete location entries
- **Google Maps Integration**: View locations on an interactive map
- **Modern UI**: Built with Jetpack Compose and Material Design 3

## 🏗️ Architecture

- **MVVM Pattern**: Separation of concerns with ViewModel and Repository
- **SQLite Database**: Local storage for location data
- **Repository Pattern**: Abstraction layer for data operations
- **Jetpack Compose**: Modern declarative UI framework

## 📋 Requirements

- Android SDK 24+ (Android 7.0)
- Target SDK 34
- Google Maps API Key (for map functionality)

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd SOFE-4640U-Assignment-2
```

### 2. Google Maps API Setup

#### Step 1: Get Google Maps API Key

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the "Maps SDK for Android" API:
   - Navigate to "APIs & Services" → "Library"
   - Search for "Maps SDK for Android"
   - Click "Enable"

#### Step 2: Create API Key

1. Go to "APIs & Services" → "Credentials"
2. Click "Create Credentials" → "API Key"
3. Copy the generated API key
4. (Recommended) Restrict the API key:
   - Click on the API key to edit
   - Under "Application restrictions", select "Android apps"
   - Add your package name: `com.example.mobile_assignment_2`
   - Under "API restrictions", select "Restrict key" and choose "Maps SDK for Android"

#### Step 3: Add API Key to Project

1. Open `app/src/main/res/values/strings.xml`
2. Replace `YOUR_API_KEY_HERE` with your actual API key:

```xml
<string name="google_maps_key">YOUR_ACTUAL_API_KEY_HERE</string>
```

### 3. Build and Run

#### Option 1: Android Studio
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Run the app on an emulator or physical device

#### Option 2: Command Line
```bash
./gradlew installDebug
```

For continuous installation during development:
```bash
./gradlew installDebug --continuous
```

### 4. Start the Emulator (if needed)
```bash
# List available emulators
emulator -list-avds

# Start the Pixel 7 Pro emulator
emulator -avd Pixel_7_Pro_API_35 &

# Verify device connection
adb devices
```

## 📊 Database Schema

The app uses a simple SQLite database with the following schema:

```sql
CREATE TABLE locations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    address TEXT NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL
);
```

## 🗺️ Pre-populated Locations

The app comes with 100 pre-populated locations across the GTA including:

- **Downtown Toronto**: CN Tower, Union Station, City Hall, etc.
- **North York**: North York Centre, York University, Finch Station, etc.
- **Scarborough**: Scarborough Town Centre, Kennedy Station, UTSC, etc.
- **Etobicoke**: Sherway Gardens, Pearson Airport, Humber College, etc.
- **Mississauga**: Square One, Port Credit, UTM, etc.
- **Brampton**: Bramalea City Centre, Downtown Brampton, Sheridan College, etc.
- **Markham**: Markham Town Centre, Unionville, York University Markham, etc.
- **Ajax**: Ajax GO Station, Community Centre, Durham College, etc.
- **Pickering**: Pickering GO Station, Seaton Community Centre, etc.
- **Oshawa**: Oshawa GO Station, UOIT, Oshawa Centre, etc.
- **Richmond Hill**: Richmond Hill Centre, Hillcrest Mall, etc.

## 🎯 Usage

### Main Features

1. **Search Locations**: Use the search bar to find specific addresses
2. **View All Locations**: Browse the complete list of GTA locations
3. **Add New Location**: Tap the + button to add custom locations
4. **Edit Locations**: Tap the edit icon on any location card
5. **Delete Locations**: Tap the delete icon to remove locations
6. **View on Map**:
   - Tap the "Map" button to view all locations
   - Tap the location icon on individual cards to view specific locations

### Navigation

- **Main Screen**: List view with search and CRUD operations
- **Map Screen**: Google Maps with location markers
- **Dialog Boxes**: Add/Edit location forms

## 🛠️ Troubleshooting

### Maps Not Loading

1. **Check API Key**: Ensure the Google Maps API key is correctly set in `strings.xml`
2. **Enable APIs**: Verify "Maps SDK for Android" is enabled in Google Cloud Console
3. **Check Restrictions**: Ensure API key restrictions (if any) include your package name
4. **Internet Connection**: Ensure the device/emulator has internet connectivity

### Build Issues

1. **Gradle Sync**: Try "File" → "Sync Project with Gradle Files"
2. **Clean Build**: Run `./gradlew clean` then rebuild
3. **Dependencies**: Ensure all required dependencies are properly downloaded

### Database Issues

- The database is automatically created and populated on first app launch
- If experiencing issues, try uninstalling and reinstalling the app

## 📝 Development Notes

### Code Structure

```
app/src/main/java/com/example/mobile_assignment_2/
├── data/
│   ├── Location.kt              # Data model
│   ├── LocationDao.kt           # Data access interface
│   ├── LocationDaoImpl.kt       # SQLite implementation
│   └── LocationDatabase.kt      # Database helper
├── repository/
│   └── LocationRepository.kt    # Repository pattern implementation
├── viewmodel/
│   └── LocationViewModel.kt     # MVVM ViewModel
├── ui/
│   └── MapScreen.kt            # Google Maps composable
└── MainActivity.kt             # Main UI and navigation
```

### Key Dependencies

- **Jetpack Compose**: Modern UI toolkit
- **Google Maps Compose**: Maps integration for Compose
- **Room Database**: Not used (using raw SQLite for educational purposes)
- **Material Design 3**: UI components and theming

## 🔧 Customization

### Adding More Locations

Modify the `populateInitialData` function in `LocationDatabase.kt` to add more pre-populated locations.

### Styling

Update theme colors in `app/src/main/res/values/themes.xml` and `ui/theme/` package.

### Map Customization

Modify `MapScreen.kt` to change map properties, marker styles, or add additional features.

## 📚 Learning Objectives Covered

- ✅ Android application development with databases
- ✅ SQLite database integration and management
- ✅ Google Maps API integration
- ✅ CRUD operations (Create, Read, Update, Delete)
- ✅ Modern Android development practices
- ✅ MVVM architecture pattern
- ✅ Jetpack Compose UI development
- ✅ Repository pattern implementation

## 🎓 Assignment Requirements Met

1. ✅ **Local Database**: SQLite database with location table (id, address, latitude, longitude)
2. ✅ **100+ GTA Locations**: Pre-populated with diverse GTA locations
3. ✅ **Query Feature**: Search functionality to find locations by address
4. ✅ **Map Display**: Google Maps integration showing locations
5. ✅ **CRUD Operations**: Complete add, edit, delete functionality
6. ✅ **Best Practices**: Clean architecture, proper naming, documentation

## 📄 License

This project is created for educational purposes as part of SOFE-4640U Mobile Application Development course.