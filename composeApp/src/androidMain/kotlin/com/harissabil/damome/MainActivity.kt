package com.harissabil.damome

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.harissabil.damome.core.object_box.ObjectBoxBackup
import com.harissabil.damome.data.local.object_box.ObjectBox
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        FileKit.init(this)

        var byteArray: ByteArray? = null

        when {
            intent?.action == Intent.ACTION_SEND -> {
                Log.d("MainActivity", "intent?.action == Intent.ACTION_SEND")
                if (intent.type?.startsWith("image/") == true) {
                    val uri = (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)
                    if (uri != null) {
                        byteArray =
                            contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }
                    }
                }
            }

            else -> {}
        }

        val backup = ObjectBoxBackup(this)

        setContent {
            CheckSystemTheme {
                App(
                    intentFilterByteArray = byteArray,
                    onBackupClick = {
                        backup
                            .database(ObjectBox.store)
                            .enableLogDebug(true)
                            .backupLocation(ObjectBoxBackup.BACKUP_LOCATION_CUSTOM_DIALOG)
                            .apply {
                                onCompleteListener { success: Boolean, message: String ->
                                    Log.d("Backup", "Backup success: $success\nmessage: $message")
                                    if (success) {
                                        restartApp(
                                            Intent(
                                                this@MainActivity,
                                                MainActivity::class.java
                                            )
                                        )
                                    }
                                }
                            }
                            .backup()
                    },
                    onRestoreClick = {
                        backup
                            .database(ObjectBox.store)
                            .enableLogDebug(true)
                            .backupLocation(ObjectBoxBackup.BACKUP_LOCATION_CUSTOM_DIALOG)
                            .apply {
                                onCompleteListener { success: Boolean, message: String ->
                                    Log.d("Restore", "Restore success: $success\nmessage: $message")
                                    if (success) {
                                        restartApp(
                                            Intent(
                                                this@MainActivity,
                                                MainActivity::class.java
                                            )
                                        )
                                    }
                                }
                            }
                            .restore()
                    }
                )
            }
        }
    }
}

@Composable
fun CheckSystemTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
            window.navigationBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    content()
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}