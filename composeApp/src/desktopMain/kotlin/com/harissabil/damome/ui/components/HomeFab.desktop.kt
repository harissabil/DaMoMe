package com.harissabil.damome.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.harissabil.damome.ui.components.speeddial_by_leinardi.FabWithLabel
import com.harissabil.damome.ui.components.speeddial_by_leinardi.SpeedDial
import com.harissabil.damome.ui.components.speeddial_by_leinardi.SpeedDialState
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
actual fun HomeFab(
    speedDialState: SpeedDialState,
    onFabClick: (Boolean) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onTypeClick: () -> Unit,
    modifier: Modifier,
) {
    SpeedDial(
        modifier = modifier,
        state = speedDialState,
        onFabClick = onFabClick,
        fabShape = MaterialTheme.shapes.large,
        fabClosedBackgroundColor = MiuixTheme.colorScheme.primary,
        fabOpenedBackgroundColor = MiuixTheme.colorScheme.primary,
        fabOpenedContentColor = MiuixTheme.colorScheme.onPrimary,
        fabClosedContentColor = MiuixTheme.colorScheme.onPrimary,
        fabOpenedContent = {
            Icon(Icons.Default.Close, "Close", tint = MiuixTheme.colorScheme.onPrimary)
        },
        fabClosedContent = {
            Icon(Icons.Default.Add, "Add", tint = MiuixTheme.colorScheme.onPrimary)
        },
        reverseAnimationOnClose = true,
    ) {
        item {
            FabWithLabel(
                onClick = { onTypeClick() },
                labelContent = { Text(text = "Type") },
                labelBackgroundColor = MiuixTheme.colorScheme.surface,
                fabShape = MaterialTheme.shapes.large,
                fabBackgroundColor = MiuixTheme.colorScheme.primary,
                fabContentColor = MiuixTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    Icons.Outlined.Keyboard,
                    null,
                    tint = MiuixTheme.colorScheme.onPrimary
                )
            }
        }
        item {
            FabWithLabel(
                onClick = { onGalleryClick() },
                labelContent = { Text(text = "Image") },
                labelBackgroundColor = MiuixTheme.colorScheme.surface,
                fabShape = MaterialTheme.shapes.large,
                fabBackgroundColor = MiuixTheme.colorScheme.primary,
                fabContentColor = MiuixTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    Icons.Outlined.Image,
                    null,
                    tint = MiuixTheme.colorScheme.onPrimary
                )
            }
        }
    }
}