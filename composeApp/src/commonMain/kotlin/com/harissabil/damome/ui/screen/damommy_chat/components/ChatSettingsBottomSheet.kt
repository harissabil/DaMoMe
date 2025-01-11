package com.harissabil.damome.ui.screen.damommy_chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.core.utils.toYyyyMmDd
import com.harissabil.damome.ui.screen.damommy_chat.ContextSize
import com.harissabil.damome.ui.screen.damommy_chat.FilterByTime
import kotlinx.datetime.LocalDate
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSettingsBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    selectedContextSize: ContextSize,
    onContextSizeSelected: (ContextSize) -> Unit,
    selectedFilterByTime: FilterByTime,
    onFilterByTimeSelected: (FilterByTime) -> Unit,
    fromDate: LocalDate,
//    onFromDateSelected: (LocalDate) -> Unit,
    toDate: LocalDate,
//    onToDateSelected: (LocalDate) -> Unit,
    isDatePickerExpanded: Boolean,
    onDatePickerExpandedChange: (Boolean) -> Unit,
) {
    ModalBottomSheet(
        containerColor = MiuixTheme.colorScheme.surface,
        scrimColor = MiuixTheme.colorScheme.surface.copy(alpha = 0.66f),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = MaterialTheme.spacing.medium)
                .padding(horizontal = MaterialTheme.spacing.medium)
                .then(modifier),
            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Text(
                modifier = Modifier.alpha(0.5f),
                text = "Answer Context Size",
                style = MiuixTheme.textStyles.subtitle,
//                color = MiuixTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            ContextSize.entries.forEach { contextSize ->
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onContextSizeSelected(contextSize) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = contextSize.display,
                            style = MiuixTheme.textStyles.body1,
                        )
                        Text(
                            modifier = Modifier.alpha(0.5f),
                            text = contextSize.description,
                            style = MiuixTheme.textStyles.body2,
                        )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    RadioButton(
                        selected = selectedContextSize == contextSize,
                        onClick = { onContextSizeSelected(contextSize) },
                        colors = RadioButtonColors(
                            selectedColor = MiuixTheme.colorScheme.primary,
                            unselectedColor = MiuixTheme.colorScheme.outline,
                            disabledSelectedColor = MiuixTheme.colorScheme.disabledPrimary,
                            disabledUnselectedColor = MiuixTheme.colorScheme.disabledPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium + MaterialTheme.spacing.small))
            Text(
                modifier = Modifier.alpha(0.5f),
                text = "Filter by Time",
                style = MiuixTheme.textStyles.subtitle,
//                color = MiuixTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            FilterByTime.entries.forEach { filterByTime ->
                if (filterByTime == FilterByTime.PICK_DATE) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFilterByTimeSelected(filterByTime) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = filterByTime.display,
                            style = MiuixTheme.textStyles.body1,
                        )
                        RadioButton(
                            selected = selectedFilterByTime == filterByTime,
                            onClick = { onFilterByTimeSelected(filterByTime) },
                            colors = RadioButtonColors(
                                selectedColor = MiuixTheme.colorScheme.primary,
                                unselectedColor = MiuixTheme.colorScheme.outline,
                                disabledSelectedColor = MiuixTheme.colorScheme.disabledPrimary,
                                disabledUnselectedColor = MiuixTheme.colorScheme.disabledPrimary
                            )
                        )
                    }
                    if (selectedFilterByTime == filterByTime) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarMonth,
                                    contentDescription = "from Date"
                                )
                                Box(modifier = modifier.height(IntrinsicSize.Min)) {
                                    TextField(
                                        label = "From",
                                        value = fromDate.toYyyyMmDd(),
                                        enabled = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        onValueChange = { },
                                        readOnly = true,
                                    )

                                    // Transparent clickable surface on top of OutlinedTextField
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 8.dp)
                                            .clip(MaterialTheme.shapes.extraSmall)
                                            .clickable(enabled = true) { onDatePickerExpandedChange(!isDatePickerExpanded) },
                                        color = Color.Transparent,
                                    ) {}
                                }
                            }
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarMonth,
                                    contentDescription = "to Date"
                                )
                                Box(modifier = modifier.height(IntrinsicSize.Min)) {
                                    TextField(
                                        label = "To",
                                        value = toDate.toYyyyMmDd(),
                                        enabled = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        onValueChange = { },
                                        readOnly = true,
                                    )

                                    // Transparent clickable surface on top of OutlinedTextField
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 8.dp)
                                            .clip(MaterialTheme.shapes.extraSmall)
                                            .clickable(enabled = true) { onDatePickerExpandedChange(!isDatePickerExpanded) },
                                        color = Color.Transparent,
                                    ) {}
                                }
                            }
                        }
                    }
                    return@forEach
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFilterByTimeSelected(filterByTime) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = filterByTime.display,
                        style = MiuixTheme.textStyles.body1,
                    )
                    RadioButton(
                        selected = selectedFilterByTime == filterByTime,
                        onClick = { onFilterByTimeSelected(filterByTime) },
                        colors = RadioButtonColors(
                            selectedColor = MiuixTheme.colorScheme.primary,
                            unselectedColor = MiuixTheme.colorScheme.outline,
                            disabledSelectedColor = MiuixTheme.colorScheme.disabledPrimary,
                            disabledUnselectedColor = MiuixTheme.colorScheme.disabledPrimary
                        )
                    )
                }
            }
        }
    }
}