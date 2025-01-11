package com.harissabil.damome.ui.screen.damommy_chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.img_damommy_avatar
import org.jetbrains.compose.resources.painterResource
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DamommyTopAppBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
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
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(Res.drawable.img_damommy_avatar),
                    contentDescription = null,
                )
                Text("DaMommy", style = MiuixTheme.textStyles.title3.copy(fontSize = 22.sp))
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateUp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate up",
                )
            }
        },
    )
}