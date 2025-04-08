package com.harissabil.damome.ui.screen.more.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.spacing
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MoreContent(
    modifier: Modifier = Modifier,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .then(modifier)
    ) {
        if (isBackupRestoreSupported()) {
            Text(
                modifier = Modifier.alpha(0.5f),
                text = "Backup & Restore",
                style = MiuixTheme.textStyles.subtitle
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            Card(
                modifier = Modifier.border(
                    1.dp,
                    MiuixTheme.colorScheme.outline,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ),
                colors = CardColors(
                    containerColor = MiuixTheme.colorScheme.surface,
                    contentColor = MiuixTheme.colorScheme.onSurface,
                    disabledContentColor = MiuixTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledContainerColor = MiuixTheme.colorScheme.surface.copy(alpha = 0.38f),
                ),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                onClick = onBackupClick
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(MaterialTheme.spacing.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CloudUpload,
                            contentDescription = "Backup",
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Text(
                                text = "Backup",
                                style = MiuixTheme.textStyles.subtitle,
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                modifier = Modifier.alpha(0.5f),
                                text = "Phone Storage",
                                style = MiuixTheme.textStyles.footnote1,
                            )
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.border(
                    1.dp,
                    MiuixTheme.colorScheme.outline,
                    RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                ),
                colors = CardColors(
                    containerColor = MiuixTheme.colorScheme.surface,
                    contentColor = MiuixTheme.colorScheme.onSurface,
                    disabledContentColor = MiuixTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledContainerColor = MiuixTheme.colorScheme.surface.copy(alpha = 0.38f),
                ),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                onClick = onRestoreClick
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(MaterialTheme.spacing.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CloudDownload,
                            contentDescription = "Restore",
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        Column(
                            modifier = Modifier.weight(2f)
                        ) {
                            Text(
                                text = "Restore",
                                style = MiuixTheme.textStyles.subtitle,
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                modifier = Modifier.alpha(0.5f),
                                text = "Phone Storage",
                                style = MiuixTheme.textStyles.footnote1,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
        Text(
            modifier = Modifier.alpha(0.5f),
            text = "About",
            style = MiuixTheme.textStyles.subtitle
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Card(
            modifier = Modifier.border(
                1.dp,
                MiuixTheme.colorScheme.outline,
                RoundedCornerShape(16.dp)
            ),
            colors = CardColors(
                containerColor = MiuixTheme.colorScheme.surface,
                contentColor = MiuixTheme.colorScheme.onSurface,
                disabledContentColor = MiuixTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledContainerColor = MiuixTheme.colorScheme.surface.copy(alpha = 0.38f),
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = onAboutClick
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Column(
                        modifier = Modifier.weight(2f)
                    ) {
                        Text(
                            text = "About DaMoMe",
                            style = MiuixTheme.textStyles.subtitle,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            modifier = Modifier.alpha(0.5f),
                            text = "Version 1.0.0-beta2",
                            style = MiuixTheme.textStyles.footnote1,
                        )
                    }
                }
            }
        }
    }
}

expect fun isBackupRestoreSupported(): Boolean