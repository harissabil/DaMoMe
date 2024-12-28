package com.harissabil.damome

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.harissabil.damome.data.local.object_box.ObjectBox
import com.harissabil.damome.di.initKoin

fun main() {
    ObjectBox.init()
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