plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.devtools.ksp)
}

/*fun getApiKey(propertyKey: String): String {
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (!localPropertiesFile.exists()) {
        throw GradleException("local.properties file not found in root project directory.")
    }
    val properties = java.util.Properties().apply {
        localPropertiesFile.inputStream().use { load(it) }
    }
    return properties.getProperty(propertyKey)
        ?: throw GradleException("$propertyKey not found in local.properties.")
}*/

android {
    namespace = "com.mmk.foodrecipe"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mmk.foodrecipe"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // TODO Need to remove the Spoonacular API Key from source control.
        // buildConfigField("String", "SPOONACULAR_API_KEY", "\"${getApiKey("spoonacular.apiKey")}\"")
        buildConfigField("String", "SPOONACULAR_API_KEY",
            "YOUR_API_KEY_HERE")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.accompanist.systemuicontroller)

    // Koin for Dependency Injection
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Ktor for Networking
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.serialization)

    // Room for Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Coil for Image Loading
    implementation(libs.coil.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}