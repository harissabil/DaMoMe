package com.harissabil.damome.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.harissabil.damome.ui.components.speeddial_by_leinardi.SpeedDialState

@Composable
expect fun HomeFab(
    speedDialState: SpeedDialState,
    onFabClick: (Boolean) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onTypeClick: () -> Unit,
    modifier: Modifier = Modifier,
)