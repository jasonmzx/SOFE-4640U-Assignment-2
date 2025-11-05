# SpotFinder - GTA Locations App

Assignment #2 for Mobile Application Development - SOFE 4640U

#### By Jason Manarroo | SN: 100825106

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
