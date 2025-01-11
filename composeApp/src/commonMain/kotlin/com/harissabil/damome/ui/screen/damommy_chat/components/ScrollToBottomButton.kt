package com.harissabil.damome.ui.screen.damommy_chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun BoxScope.ScrollToBottomButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight }
        ),
        exit = fadeOut() + slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight }
        ),
        modifier = Modifier
            .padding(bottom = 16.dp)
            .align(Alignment.BottomCenter)
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = modifier,
            colors = IconButtonDefaults.filledIconButtonColors().copy(
                containerColor = MiuixTheme.colorScheme.primaryContainer,
                contentColor = MiuixTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Scroll to top"
            )
        }
    }
}