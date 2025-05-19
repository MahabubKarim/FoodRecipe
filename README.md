## 🥄 FoodRecipe - A Modern Android Recipe App

FoodRecipe is a full-fledged Android application built to help users discover, search, view details, and save their favorite recipes. It leverages the **Spoonacular API** for a rich database of culinary delights and is constructed using modern Android development practices and libraries.

This project serves as a comprehensive example of building an app with:
*   **MVVM (Model-View-ViewModel) Architecture:** Clear separation of concerns.
*   **Layered Architecture:** Following Google's recommended guidelines with distinct **Domain**, **Data**, and **UI** layers.
*   **100% Kotlin:** For concise, safe, and modern code.
*   **Jetpack Compose:** For building a reactive and declarative UI.

### ✨ Features:

*   🍳 **Discover Recipes:** Browse a list of random recipes for inspiration.
*   🔍 **Search Functionality:** Find recipes based on keywords.
*   📖 **Detailed Recipe View:** Get comprehensive information including ingredients, instructions, cooking time, and servings.
*   ❤️ **Favorites:** Save your most-loved recipes locally for quick offline access.
*   📱 **Responsive UI:** Clean and intuitive user interface built with Jetpack Compose.

### 🛠️ Tech Stack & Architecture:

*   **Architecture:**
    *   **MVVM (Model-View-ViewModel)**
    *   **Layered Architecture:** Domain, Data (Repository, Local, Remote), UI (ViewModel, Screen)
*   **UI:**
    *   **Jetpack Compose:** For declarative UI development.
    *   **Coil:** For efficient image loading and caching.
    *   **Material 3:** For modern UI components and theming.
    *   **Compose Navigation:** For navigating between screens.
*   **Data & Networking:**
    *   **Ktor Client:** For making HTTP requests to the Spoonacular API.
    *   **Kotlinx Serialization:** For parsing JSON data.
    *   **Room Persistence Library:** For local database storage (favorites).
*   **Dependency Injection:**
    *   **Koin:** For managing dependencies throughout the application.
*   **Asynchronous Programming:**
    *   **Kotlin Coroutines & Flow:** For managing background tasks and reactive data streams.
*   **Build & Configuration:**
    *   **Gradle (Kotlin DSL)**
    *   **BuildConfig:** For securely handling API keys.

### 🚀 Getting Started:

1.  Clone the repository:
    ```bash
    git clone https://github.com/MahabubKarim/FoodRecipe.git
    ```
2.  Create a `local.properties` file in the root project directory.
3.  Add your Spoonacular API key to `app/build.gradle.kts`:
    ```properties
    buildConfigField("String", "SPOONACULAR_API_KEY",
            "YOUR_API_KEY_HERE")
    ```
5.  Open the project in Android Studio (latest stable version recommended).
6.  Build and run the application on an emulator or physical device.

### 📝 Code Structure:

The project follows Google's recommended app architecture guidelines:

*   `com.mmk.foodrecipe`
    *   `data/`: Contains data sources (remote and local), repositories, and DTOs/Entities.
        *   `local/`: Room database, DAOs, and local entity models.
        *   `remote/`: Ktor client, API service definitions, and DTOs.
        *   `repository/`: Implementation of the repository pattern.
        *   `mappers/`: Functions to map between DTOs, Entities, and Domain models.
    *   `di/`: Koin modules for dependency injection.
    *   `domain/`: Core business logic, use cases (interactors), and domain models.
        *   `model/`: Plain Kotlin Objects representing core business entities.
        *   `repository/`: Interfaces for repositories.
        *   `usecase/`: Business logic classes.
    *   `ui/`: Jetpack Compose UI elements, ViewModels, and navigation.
        *   `navigation/`: Compose Navigation setup.
        *   `screen/`: Composable functions for different app screens.
        *   `theme/`: App theming (colors, typography).
        *   `viewmodel/`: ViewModels for each screen.
    *   `FoodRecipeApp.kt`: Application class for Koin initialization.
    *   `MainActivity.kt`: To initialize the Activity and show the initial screen.
