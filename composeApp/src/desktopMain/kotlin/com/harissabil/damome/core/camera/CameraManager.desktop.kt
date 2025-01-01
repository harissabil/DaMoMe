package com.harissabil.damome.core.camera

import androidx.compose.runtime.Composable

@Composable
actual fun rememberCameraManager(onResult: (SharedImage?) -> Unit): CameraManager {
    return CameraManager({})
}

actual class CameraManager actual constructor(onLaunch: () -> Unit) {
    actual fun launch() {
    }
}