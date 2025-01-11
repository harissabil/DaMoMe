package com.harissabil.damome.ui.screen.home.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.errorDark
import com.harissabil.damome.core.theme.errorLight
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.core.utils.formatCurrency
import com.harissabil.damome.core.utils.toReadableTime
import com.harissabil.damome.domain.model.Category.Companion.toCategory
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.TransactionType
import com.harissabil.damome.ui.components.BaseCard
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun TransactionDayItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onEditClick: (transaction: Transaction) -> Unit,
    onDeleteClick: (transaction: Transaction) -> Unit,
) {
    val category = transaction.category.toCategory()

    val prefix = if (transaction.type == TransactionType.INCOME) "+" else "-"

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
                        }
                    )
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(MaterialTheme.spacing.medium),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = transaction.timestamp.toReadableTime(),
                    style = MiuixTheme.textStyles.subtitle,
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 1.dp,
                    color = category.color
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Column(
                    modifier = Modifier.weight(2f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = category.icon, contentDescription = null,
                            tint = category.color,
                        )
                        Text(
                            text = category.display,
                            style = MiuixTheme.textStyles.subtitle,
                        )
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = if (transaction.description.isNullOrBlank()) "-" else transaction.description,
                        style = MiuixTheme.textStyles.footnote1,
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Text(
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                    textAlign = TextAlign.End,
                    text = "$prefix${transaction.currency} ${
                        formatCurrency(
                            transaction.currency,
                            transaction.amount
                        )
                    }",
                    color = if (transaction.type == TransactionType.INCOME) MiuixTheme.colorScheme.primary else
                        if (isSystemInDarkTheme()) errorDark else errorLight,
                    style = MiuixTheme.textStyles.subtitle
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
                        onEditClick(transaction)
                        isContextMenuVisible = false
                    },
                    text = { Text(text = "Edit") },
                    colors = MenuItemColors(
                        textColor = MiuixTheme.colorScheme.onSurface,
                        leadingIconColor = MiuixTheme.colorScheme.onSurface,
                        trailingIconColor = MiuixTheme.colorScheme.onSurface,
                        disabledTextColor = MiuixTheme.colorScheme.onSurface,
                        disabledLeadingIconColor = MiuixTheme.colorScheme.onSurface,
                        disabledTrailingIconColor = MiuixTheme.colorScheme.onSurface,
                    )
                )
                DropdownMenuItem(
                    onClick = {
                        onDeleteClick(transaction)
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