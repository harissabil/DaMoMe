package com.harissabil.damome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.harissabil.damome.core.theme.DaMoMeTheme
import com.harissabil.damome.ui.navigation.NavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import top.yukonga.miuix.kmp.basic.Surface

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App(
    intentFilterByteArray: ByteArray? = null,
    onBackupClick: () -> Unit = {},
    onRestoreClick: () -> Unit = {},
) {
    KoinContext {
        DaMoMeTheme {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                val viewModel = koinViewModel<MainViewModel>()

                NavGraph(
                    startDestination = viewModel.startRoute,
                    intentFilterByteArray = intentFilterByteArray,
                    onBackupClick = onBackupClick,
                    onRestoreClick = onRestoreClick,
                )
            }
        }
    }
}