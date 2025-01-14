plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    id ("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.colab.myfriend"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.colab.myfriend"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
        }
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
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.databinding.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.material.v1100)

    implementation(libs.androidx.core.ktx.v190)
    implementation(libs.androidx.appcompat.v151)
    implementation(libs.material.v161)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.lifecycle.livedata.ktx.v251)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v251)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.room.runtime.v250)

    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.glide)

    implementation (libs.androidcoreproject)

    annotationProcessor(libs.compiler)

    implementation(libs.hilt.android)
    ksp (libs.dagger.compiler)
    ksp (libs.hilt.compiler)

    annotationProcessor (libs.hilt.compiler.v252)

    testImplementation (libs.hilt.android.testing)
    kaptTest (libs.hilt.compiler.v252)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom.v3311))
    implementation(libs.firebase.crashlytics)
    implementation(libs.google.firebase.analytics)

    implementation (libs.timber)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)


}

ksp {
        arg("room.schemaLocation", "$projectDir/schemas")

}

kapt {
    correctErrorTypes = true
}
