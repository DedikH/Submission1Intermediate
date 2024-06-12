plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.submission1intermediate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.submission1intermediate"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.mlkit:common:18.10.0")
    implementation("androidx.datastore:datastore-core-android:1.1.1")
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.google.android.ads:mediation-test-suite:3.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("com.google.android.gms:play-services-ads:21.0.0")

//    Text Input Layout
    implementation("com.google.android.material:material:1.1.0")

//    Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

//    Data Store
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")

//    Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.5.0-alpha06")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0-alpha06")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0-alpha06")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0-alpha06")

//    List Story Round ImG
    implementation("androidx.cardview:cardview:1.0.0")

//    Glide IMG
    implementation("com.github.bumptech.glide:glide:4.16.0")
}