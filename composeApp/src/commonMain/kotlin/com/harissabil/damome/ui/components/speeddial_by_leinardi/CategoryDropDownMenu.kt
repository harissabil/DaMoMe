package com.harissabil.damome.ui.components.speeddial_by_leinardi

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.harissabil.damome.core.theme.DaMoMeTheme
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.domain.model.Category
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.LazyColumn
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.HorizontalDivider

@Composable
fun CategoryDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    notSetLabel: String? = null,
    items: List<Category>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: Category) -> Unit,
    selectedItemToString: (Category) -> String = { it.display },
    drawItem: @Composable (Category, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        CategoryDropdownMenuItem(
            text = item.display,
            icon = item.icon,
            iconColor = item.color,
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    val selectedItemColor: Color? = items.getOrNull(selectedIndex)?.color
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.height(IntrinsicSize.Min)) {
        TextField(
            label = label,
            value = items.getOrNull(selectedIndex)?.let { selectedItemToString(it) } ?: "",
            enabled = enabled,
            modifier = Modifier.fillMaxWidth().border(
                1.dp,
                selectedItemColor ?: Color.Transparent,
                RoundedCornerShape(16.dp)
            ),
            leadingIcon = {
                items.getOrNull(selectedIndex)?.icon?.let {
                    Row {
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        Icon(
                            imageVector = it,
                            contentDescription = "",
                            tint = selectedItemColor ?: Color.Transparent
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    }
                }
            },
            trailingIcon = {
                Row {
                    val icon =
                        if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
                    Icon(imageVector = icon, contentDescription = "")
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                }
            },
            onValueChange = { },
            readOnly = true
        )

        // Transparent clickable surface on top of OutlinedTextField
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = enabled) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            DaMoMeTheme {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MiuixTheme.colorScheme.surface,
                ) {
                    val listState = rememberLazyListState()
                    if (selectedIndex > -1) {
                        LaunchedEffect("ScrollToSelected") {
                            listState.scrollToItem(index = selectedIndex)
                        }
                    }

                    LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                        if (notSetLabel != null) {
                            item {
                                CategoryDropdownMenuItem(
                                    text = notSetLabel,
                                    selected = false,
                                    enabled = false,
                                    onClick = { },
                                )
                            }
                        }
                        itemsIndexed(items) { index, item ->
                            val selectedItem = index == selectedIndex
                            drawItem(
                                item,
                                selectedItem,
                                true
                            ) {
                                onItemSelected(index, item)
                                expanded = false
                            }

                            if (index < items.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = DividerDefaults.Thickness,
                                    color = MiuixTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDropdownMenuItem(
    text: String,
    icon: ImageVector? = null,
    iconColor: Color? = null,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38F)
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = 1.00F)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 1.00F)
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        tint = iconColor ?: contentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = text,
                        style = MiuixTheme.textStyles.subtitle,
                    )
                }
            }
        }
    }
}