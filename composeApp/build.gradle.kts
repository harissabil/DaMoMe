import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.kotlin.serialization)
//    id("io.objectbox")
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        if (!gradle.startParameter.taskNames.any { it.contains("composeApp:run") }) {
            apply(plugin = "kotlin-kapt")
        }

//        dependencies {
//            debugImplementation("io.objectbox:objectbox-android-objectbrowser:4.0.3")
//        }
        apply(plugin = "io.objectbox")
    }

//    if (!gradle.startParameter.taskNames.any { it.contains("composeApp:run") }) {
//
//    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")
//    {
//        tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptTask> {
//            enabled = false
//        }
//    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        extraSpecAttributes["swift_version"] = "\"5.0\""

        framework {
            baseName = "ComposeApp"
            isStatic = true
            embedBitcode(BitcodeEmbeddingMode.BITCODE)
        }

        pod("ObjectBox")
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Splash Screen
            implementation("androidx.core:core-splashscreen:1.0.1")

            // Ktor
            implementation(libs.ktor.client.okhttp)

            // Koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            // ObjectBox
            if (!gradle.startParameter.taskNames.any { it.contains("composeApp:run") }) {
                configurations["kapt"].dependencies.add(project.dependencies.create("io.objectbox:objectbox-processor:4.0.3"))
            }
            implementation("io.objectbox:objectbox-kotlin:4.0.3")

            // Accompanist Permission
            implementation("com.google.accompanist:accompanist-permissions:0.37.0")

            // Apache I/O
            implementation("commons-io:commons-io:2.18.0")

            // Material Design Implementation
            implementation("com.google.android.material:material:1.12.0")

            // ReLinker
            implementation("com.getkeepsafe.relinker:relinker:1.4.4")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Icon Extended
            implementation(compose.materialIconsExtended)

            // DateTime
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

            // ViewModel
            implementation(libs.viewmodel.compose)

            // Navigation
            implementation(libs.navigation.compose)
            implementation(compose.material3AdaptiveNavigationSuite)

            // Adaptive
            implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.0.0")
            implementation("org.jetbrains.compose.material3.adaptive:adaptive-layout:1.0.0-alpha03")
            implementation("org.jetbrains.compose.material3.adaptive:adaptive-navigation:1.0.0-alpha03")
            implementation("org.jetbrains.compose.material3:material3-window-size-class:1.7.3")

            // Miuix UI Library
            implementation(libs.miuix)

            // Google Generative AI SDK
            implementation(libs.generativeai.google)

            // Ktor Client
            implementation(libs.bundles.ktor)

            // Koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Constraint Layout
            implementation("tech.annexflow.compose:constraintlayout-compose-multiplatform:0.4.0")

            // Calendar
            implementation("com.kizitonwose.calendar:compose-multiplatform:2.6.1")

            // Date Time Picker
            implementation("network.chaintech:kmp-date-time-picker:1.0.7")

            // FileKit
            implementation("io.github.vinceglb:filekit-compose:0.8.8")

            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            // Ktor
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.harissabil.damome"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.harissabil.damome"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 7
        versionName = "1.0.0-beta3"
    }
    bundle {
        abi {
            enableSplit = false
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
    buildFeatures {
        buildConfig = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    ksp(libs.room.compiler)
}

compose.desktop {
    application {
        mainClass = "com.harissabil.damome.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "com.harissabil.damome"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptTask>().configureEach {
    if (name.contains("Desktop", ignoreCase = true)) {
        enabled = false
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptWithoutKotlincTask>().configureEach {
    if (name.contains("Desktop", ignoreCase = true)) {
        enabled = false
    }
}