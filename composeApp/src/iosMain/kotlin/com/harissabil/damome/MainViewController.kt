package com.harissabil.damome

import androidx.compose.ui.window.ComposeUIViewController
import com.harissabil.damome.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}