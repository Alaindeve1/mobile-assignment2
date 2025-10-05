# Assignment 2 - Android Application

## Project Overview
This is an Android application developed as part of a mobile programming assignment. The app includes user authentication and a dashboard interface.

## Features
- User login functionality
- Dashboard navigation
- Modern UI with Material Design components
- Form validation
- Responsive layout

## Tech Stack
- **Language**: Java
- **Minimum SDK**: API 24 (Android 7.0)
- **Architecture**: MVC (Model-View-Controller)
- **Libraries**:
  - AndroidX
  - Material Design Components

## Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- Android SDK 24 or higher
- Java Development Kit (JDK) 11 or higher

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Run the app on an emulator or physical device

### Building the Project
1. In Android Studio, click on `Build` > `Make Project`
2. Or use the Gradle wrapper: `./gradlew build`

## Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/assignment2/
│   │   │   ├── LoginActivity.java    # Handles user authentication
│   │   │   └── DashboardActivity.java # Main dashboard after login
│   │   └── res/                      # Resources (layouts, strings, etc.)
│   └── androidTest/                  # Instrumented tests
└── build.gradle                      # Module-level build configuration
```

## Screenshots
*(Add screenshots of your app here)*

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments
- AUCA Mobile Programming Course
- Android Developer Documentation
