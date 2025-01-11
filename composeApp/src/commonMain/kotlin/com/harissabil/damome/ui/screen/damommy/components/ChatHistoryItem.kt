package com.harissabil.damome.ui.screen.damommy.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.core.utils.toReadableDateTime
import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.ui.components.BaseCard
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun ChatHistoryItem(
    modifier: Modifier = Modifier,
    chatGroup: ChatGroup,
    onClick: (chatGroupId: Long) -> Unit,
    onDeleteClick: (chatGroupId: Long) -> Unit,
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current

    BaseCard(
        modifier = modifier.onSizeChanged {
            itemHeight = with(density) { it.height.toDp() }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        },
                        onTap = {
                            isContextMenuVisible = false
                            if (!isContextMenuVisible) {
                                onClick(chatGroup.id!!)
                            }
                        }
                    )
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(MaterialTheme.spacing.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = chatGroup.name,
                        style = MiuixTheme.textStyles.subtitle,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = chatGroup.timestamp.toReadableDateTime(),
                        style = MiuixTheme.textStyles.footnote1,
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Icon(
                    imageVector = Icons.Default.NavigateNext,
                    contentDescription = null,
                )
            }

            DropdownMenu(
                expanded = isContextMenuVisible,
                onDismissRequest = {
                    isContextMenuVisible = false
                },
                offset = pressOffset.copy(
                    y = pressOffset.y - itemHeight
                ),
                containerColor = MiuixTheme.colorScheme.surface,
            ) {
                DropdownMenuItem(
                    onClick = {
                        onDeleteClick(chatGroup.id!!)
                        isContextMenuVisible = false
                    },
                    text = { Text(text = "Delete") },
                    colors = MenuItemColors(
                        textColor = MiuixTheme.colorScheme.onSurface,
                        leadingIconColor = MiuixTheme.colorScheme.onSurface,
                        trailingIconColor = MiuixTheme.colorScheme.onSurface,
                        disabledTextColor = MiuixTheme.colorScheme.onSurface,
                        disabledLeadingIconColor = MiuixTheme.colorScheme.onSurface,
                        disabledTrailingIconColor = MiuixTheme.colorScheme.onSurface,
                    )
                )
            }
        }
    }
}