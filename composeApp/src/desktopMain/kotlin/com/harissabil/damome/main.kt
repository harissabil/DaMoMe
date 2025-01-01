package com.harissabil.damome

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.harissabil.damome.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "DaMoMe",
        ) {
            App()
        }
    }
}