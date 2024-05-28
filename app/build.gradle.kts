plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
}

val aarVersion = "1.0"

android {
    namespace = "com.estudio.oss_dns_resolver_v1"
    compileSdk = 34

    defaultConfig {
//        applicationId = "com.estudio.oss_dns_resolver_v1"
//        minSdk = 24
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    libraryVariants.all {
        this.outputs.map { output ->
            val outputFile = output.outputFile
            if(output != null && outputFile.name.endsWith(".aar")) {
                val fileName = "oss-resolver-${aarVersion}.aar"
                outputFile.renameTo(File(outputFile.parent, fileName))
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dependencies
    implementation(libs.google.gson)
    implementation(libs.retrofit)
    implementation(libs.oktHttp)
//    implementation(libs.oktHttpInterceptor)
    implementation(libs.retrofit.gson)
    implementation(libs.kotlin.serializable)
//    implementation(files("aar/HTTPDNS_Android_v4.9.0a_release.aar"))

    // RX Java
    implementation(libs.rxjava)
    implementation(libs.rx.android)
    implementation(libs.retrofit.rxjava.converter)
    // DNS
    implementation(project(":aar", configuration = "dnsProvider"))

    //AndroidUtilCode
    implementation(libs.util.code) {
        exclude(group = "com.android.support")
        exclude(group = "com.google.code.gson")
    }


}