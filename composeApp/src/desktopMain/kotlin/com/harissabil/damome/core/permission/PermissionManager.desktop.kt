package com.harissabil.damome.core.permission

import androidx.compose.runtime.Composable

actual class PermissionsManager actual constructor(callback: PermissionCallback) : PermissionHandler {
    @Composable
    override fun askPermission(permission: PermissionType) {

    }

    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return true
    }

    @Composable
    override fun launchSettings() {

    }
}

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return PermissionsManager(callback)
}