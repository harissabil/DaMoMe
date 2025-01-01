package com.harissabil.damome.ui.components.speeddial_by_leinardi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SpeedDialOverlay(
    visible: Boolean,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
    color: Color = MiuixTheme.colorScheme.surface.copy(alpha = 0.66f),
    animate: Boolean = true,
) {
    if (animate) {
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            OverlayBox(color, onClick, modifier)
        }
    } else if (visible) {
        OverlayBox(color, onClick, modifier)
    }
}

@Composable
private fun OverlayBox(
    color: Color,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .then(modifier),
    )
}