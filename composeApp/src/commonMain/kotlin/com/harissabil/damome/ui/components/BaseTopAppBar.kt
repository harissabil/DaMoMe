package com.harissabil.damome.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateUp: (() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarColors(
            containerColor = MiuixTheme.colorScheme.background,
            titleContentColor = MiuixTheme.colorScheme.onBackground,
            navigationIconContentColor = MiuixTheme.colorScheme.onBackground,
            scrolledContainerColor = MiuixTheme.colorScheme.background,
            actionIconContentColor = MiuixTheme.colorScheme.onBackground,
        ),
        title = { Text(title, style = MiuixTheme.textStyles.title3.copy(fontSize = 22.sp)) },
        navigationIcon = {
            onNavigateUp?.let {
                IconButton(
                    onClick = it
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate up",
                    )
                }
            }
        },
    )
}