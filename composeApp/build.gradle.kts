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
    id("io.objectbox")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    if (gradle.startParameter.taskNames.contains("desktop:run")) {
        println("Running without kapt")
    } else {
        println("Running with kapt for task ${gradle.startParameter.taskNames.firstOrNull()}")
        apply(plugin = "kotlin-kapt")
    }

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

            // Ktor
            implementation(libs.ktor.client.okhttp)

            // Koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            // ObjectBox
            configurations["kapt"].dependencies.add(project.dependencies.create("io.objectbox:objectbox-processor:4.0.3"))
            implementation("io.objectbox:objectbox-kotlin:4.0.3")
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

            // DateTime
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

            // ViewModel
            implementation(libs.lifecycle.viewmodel)

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

            // Ktor
            implementation(libs.bundles.ktor)

            // Koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            // ObjectBox JVM
            implementation(libs.objectbox.java)

            // Ktor
            implementation(libs.ktor.client.okhttp)

            // ObjectBox
            implementation("io.objectbox:objectbox-kotlin:4.0.3")
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
        versionCode = 1
        versionName = "1.0.0"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.harissabil.damome.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.harissabil.damome"
            packageVersion = "1.0.0"
        }
    }
}
