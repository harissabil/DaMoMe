package com.harissabil.damome.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.spacing
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun CustomSnackbar(
    modifier: Modifier = Modifier,
    message: String,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Snackbar(
        modifier = modifier.padding(MaterialTheme.spacing.medium).border(
            1.dp,
            MiuixTheme.colorScheme.outline,
            RoundedCornerShape(16.dp)
        ),
        containerColor = MiuixTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
    ) {
        CompositionLocalProvider(
            LocalLayoutDirection provides
                    if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {
            Row(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Icon(
//                    imageVector = MiuixIcons.Info,
//                    tint = when (type) {
//                        SnackbarType.Info -> {
//                            MiuixTheme.colorScheme.primary
//                        }
//
//                        SnackbarType.Error -> {
//                            if (isSystemInDarkTheme()) errorDark else errorLight
//                        }
//                    },
//                    contentDescription = null
//                )
//                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Text(message)
            }
        }
    }
}

@Composable
fun CustomSnackbarHost(
    hostState: SnackbarHostState,
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { data ->
            CustomSnackbar(message = data.visuals.message)
        }
    )
}