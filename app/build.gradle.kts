import com.android.build.gradle.internal.api.BaseVariantOutputImpl

buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}


plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    id("maven-publish")
}

android {
    namespace = "com.estudio.oss_dns_resolver_v1"
    compileSdk = 34

    defaultConfig {
//        applicationId = "com.estudio.oss_dns_resolver_v1"
        minSdk = 24
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

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(18)
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
        val variant = this
        variant.outputs
            .map { it as BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName = "oss_dns_resolver.aar"
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.NotEfraim"
            artifactId = "OSS_DNS_Resolver"
            version = "1.13"

            pom {
                name.set("OSS DNS Resolver")
                description.set("Developed by Efraim")
                url.set("https://github.com/NotEfraim/OSS_DNS_Resolver")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("Efraim")
                        name.set("John Efraim Canilang")
                        email.set("efraimcanilang@gmail.com")
                    }
                }
            }

            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")

                // Function to add dependencies from a configuration
                fun addDependencies(configurationName: String, scope: String) {
                    configurations[configurationName].allDependencies.forEach {
                        if (it.group != null && it.version != null) {
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                            dependencyNode.appendNode("scope", scope)
                        }
                    }
                }

                // Add dependencies from the 'api' and 'implementation' configurations
                addDependencies("api", "compile")
                addDependencies("implementation", "runtime")
                // Add other configurations if needed
                // addDependencies("compileOnly", "provided")
            }
        }
    }

    repositories {
        mavenLocal()
        // Add other repositories if needed
         maven {
             url = uri("https://github.com/NotEfraim/OSS_DNS_Resolver")
             credentials {
                 username = project.findProperty("repoUser") as String? ?: ""
                 password = project.findProperty("repoPassword") as String? ?: ""
             }
         }
    }
}
