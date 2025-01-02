package com.harissabil.damome.ui.screen.more

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.ui.components.BaseTopAppBar
import com.harissabil.damome.ui.components.CustomSnackbarHost
import com.harissabil.damome.ui.screen.more.components.MoreContent
import top.yukonga.miuix.kmp.basic.Scaffold

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        topBar = { BaseTopAppBar(title = "More") }
    ) { innerPadding ->
        MoreContent(
            modifier = modifier
                .padding(innerPadding)
                .padding(vertical = MaterialTheme.spacing.small)
                .padding(horizontal = MaterialTheme.spacing.medium),
            onBackupClick = onBackupClick,
            onRestoreClick = onRestoreClick,
            onAboutClick = { uriHandler.openUri("https://github.com/harissabil/DaMoMe") }
        )
    }
}