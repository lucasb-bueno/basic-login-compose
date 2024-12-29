# BasicLogin Android App

[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-%2300C853.svg?style=flat&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Firebase Auth](https://img.shields.io/badge/Firebase%20Auth-%23FFCA28.svg?style=flat&logo=firebase&logoColor=white)](https://firebase.google.com/products/auth)
[![Hilt](https://img.shields.io/badge/Hilt-DI-%230EA5E9.svg?style=flat&logo=google&logoColor=white)](https://developer.android.com/training/dependency-injection/hilt)
[![FireStore](https://img.shields.io/badge/FireStore-%23FF6F00.svg?style=flat&logo=firebase&logoColor=white)](https://firebase.google.com/products/firestore)

## About

**BasicLogin** is an Android application designed as a base architecture for integrating authentication into any app. The project leverages modern Android development tools and libraries to provide a clean, maintainable, and scalable codebase.

The app includes:
- **Jetpack Compose** for a fully declarative and modern UI.
- **Firebase Authentication** for managing user login.
- **Google Sign-In** as the initial login option.
- **FireStore** for database integration.
- **Hilt** for dependency injection and simplified code.

This project is a foundation for adding login features into other applications, making it easy to integrate Firebase Authentication and manage user sessions.

---

## Features

- 🔒 **Secure Authentication**: Firebase Authentication with Google Sign-In.
- 🛠️ **Scalable Architecture**: Modular and clean architecture design to serve as a base for future apps.
- 📦 **Dependency Injection**: Powered by Hilt to ensure easy dependency management.
- 🗂️ **Cloud Database**: FireStore integration for storing and managing user-related data.
- 💡 **Modern Android Development**: Built with Jetpack Compose for a smooth and responsive UI.

---

## Tech Stack

### Core Libraries and Tools:
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - For building modern UI.
- [Firebase Authentication](https://firebase.google.com/products/auth) - For handling user login and registration.
- [Google Sign-In](https://developers.google.com/identity/sign-in/android) - For simple and seamless user authentication.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - For dependency injection.
- [FireStore](https://firebase.google.com/products/firestore) - For database and user data management.

### Future Integrations:
- Additional libraries and features will be added to extend the app's functionality.

---

## Architecture

The app is built using **Clean Architecture**, ensuring a modular and maintainable structure:
- **Presentation Layer**: Built with Jetpack Compose, including state management.
- **Domain Layer**: Encapsulates business logic.
- **Data Layer**: Handles interactions with Firebase Authentication and FireStore.

---

## How to Use

1. Clone this repository:
   ```bash
   git clone https://github.com/lucasb-bueno/basic-login-compose.git

   - Add your google-services.json file into app folder
   - Create your Constant.kt file and add your variables

