plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ucaldas.ciclodevida"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.ucaldas.ciclodevida"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    // ComponentActivity + registerForActivityResult (API moderna de resultados)
    implementation("androidx.activity:activity-ktx:1.13.0")
}
