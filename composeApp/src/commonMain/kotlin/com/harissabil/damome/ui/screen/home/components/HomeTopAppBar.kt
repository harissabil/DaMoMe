package com.harissabil.damome.ui.screen.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.damome_logo
import org.jetbrains.compose.resources.painterResource
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarColors(
            containerColor = MiuixTheme.colorScheme.background,
            titleContentColor = MiuixTheme.colorScheme.onBackground,
            navigationIconContentColor = MiuixTheme.colorScheme.onBackground,
            scrolledContainerColor = MiuixTheme.colorScheme.background,
            actionIconContentColor = MiuixTheme.colorScheme.onBackground,
        ),
        navigationIcon = {
            Row {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(Res.drawable.damome_logo),
                    contentDescription = null
                )
            }
        },
        title = {}
    )
}