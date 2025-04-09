package com.harissabil.damome.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.errorDark
import com.harissabil.damome.core.theme.errorLight
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.core.utils.formatToLocalizedString
import com.harissabil.damome.core.utils.toHhMm
import com.harissabil.damome.core.utils.toYyyyMmDd
import com.harissabil.damome.domain.model.Category
import com.harissabil.damome.domain.model.TransactionType
import com.harissabil.damome.ui.components.speeddial_by_leinardi.CategoryDropdownMenu
import kotlinx.datetime.LocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    currency: Currency,
    amount: Double,
    scannedAmount: Double?,
    onAmountChange: (String) -> Unit,
    dateAndTime: LocalDateTime,
    onDateAndTimeChange: (LocalDateTime) -> Unit,
    isDateTimePickerExpanded: Boolean,
    onDateTimePickerExpandedChange: (Boolean) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    description: String?,
    onDescriptionChange: (String) -> Unit,
    transactionType: TransactionType,
    onTransactionTypeChange: (TransactionType) -> Unit,
    isLoading: Boolean,
    submitText: String = "Save",
    onSubmitTransaction: () -> Unit,
) {
    var isCurrentDateTimeChecked by rememberSaveable { mutableStateOf(false) }

    val categories = Category.entries.toTypedArray()

    var isAmountError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        containerColor = MiuixTheme.colorScheme.surface,
        scrimColor = MiuixTheme.colorScheme.surface.copy(alpha = 0.66f),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.medium)
                .padding(horizontal = MaterialTheme.spacing.medium)
                .then(modifier),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Text(
                modifier = Modifier.alpha(0.5f),
                text = "Amount",
                style = MiuixTheme.textStyles.subtitle
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                Text(
                    text = currency.symbol,
                    style = MiuixTheme.textStyles.headline2
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
//                    TextField(
//                        modifier = Modifier.takeIf { isAmountError }?.then(
//                            Modifier.border(
//                                1.dp,
//                                if (isSystemInDarkTheme()) errorDark else errorLight,
//                                RoundedCornerShape(16.dp)
//                            )
//                        )
//                            ?: Modifier,
//                        value = if (amount == 0.0) "" else amount.toLong().toString(),
//                        onValueChange = {
//                            val sanitizedValue = it.replace(Regex("[^0-9.]"), "")
//                            onAmountChange(sanitizedValue)
//                        },
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.NumberPassword,
//                            imeAction = ImeAction.Next
//                        ),
//                        visualTransformation = DecimalAmountTransformation(currency)
//                    )
                    CurrencyTextField(
                        modifier = Modifier.takeIf { isAmountError }?.then(
                            Modifier.border(
                                1.dp,
                                if (isSystemInDarkTheme()) errorDark else errorLight,
                                RoundedCornerShape(16.dp)
                            )
                        )
                            ?: Modifier,
                        initialText = if (amount == 0.0) "" else formatToLocalizedString(amount, currency),
                        scannedAmount = scannedAmount?.let { formatToLocalizedString(it, currency) },
                        currency = currency,
                        onChange = {
                            println("amount to be changed: $it")
                            onAmountChange(it)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                    )
                    if (isAmountError) {
                        Text(
                            text = "Please enter a valid amount",
                            style = MiuixTheme.textStyles.footnote1,
                            color = if (isSystemInDarkTheme()) errorDark else errorLight
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.alpha(0.5f),
                text = "Date & Time",
                style = MiuixTheme.textStyles.subtitle
            )
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
                        contentDescription = "Date"
                    )
                    Box(modifier = modifier.height(IntrinsicSize.Min)) {
                        TextField(
                            value = dateAndTime.date.toYyyyMmDd(),
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
                                .clickable(enabled = true) { onDateTimePickerExpandedChange(!isDateTimePickerExpanded) },
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
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = "Time"
                    )
                    Box(modifier = modifier.height(IntrinsicSize.Min)) {
                        TextField(
                            value = dateAndTime.time.toHhMm(),
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
                                .clickable(enabled = true) { onDateTimePickerExpandedChange(!isDateTimePickerExpanded) },
                            color = Color.Transparent,
                        ) {}
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isCurrentDateTimeChecked = if (!isCurrentDateTimeChecked) {
                                onDateAndTimeChange(LocalDateTime.now())
                                true
                            } else {
                                false
                            }
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                Checkbox(
                    checked = isCurrentDateTimeChecked,
                    onCheckedChange = {
                        isCurrentDateTimeChecked = if (!isCurrentDateTimeChecked) {
                            onDateAndTimeChange(LocalDateTime.now())
                            true
                        } else {
                            false
                        }
                    }
                )
                Text(
                    text = "Current date and time",
                    style = MiuixTheme.textStyles.footnote1
                )
            }

            Text(
                modifier = Modifier.alpha(0.5f),
                text = "Category",
                style = MiuixTheme.textStyles.subtitle
            )
            CategoryDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                items = Category.entries,
                label = "",
                selectedIndex = categories.indexOfFirst { it.value == category },
                onItemSelected = { index, item ->
                    onCategoryChange(item.value)
                }
            )

            Text(
                modifier = Modifier.alpha(0.5f),
                text = "Description",
                style = MiuixTheme.textStyles.subtitle
            )
            TextField(
                value = description ?: "",
                onValueChange = onDescriptionChange,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall)
            ) {
                FilterChip(
                    modifier = Modifier.weight(1f).height(48.dp),
                    label = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "Income",
                            style = MiuixTheme.textStyles.headline2,
                            color = if (transactionType == TransactionType.INCOME) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.outline
                        )
                    },
                    selected = transactionType == TransactionType.INCOME,
                    onClick = { onTransactionTypeChange(TransactionType.INCOME) },
                    colors = FilterChipDefaults.filterChipColors().copy(
                        containerColor = MiuixTheme.colorScheme.surface,
                        selectedContainerColor = MiuixTheme.colorScheme.surface,
                        selectedLabelColor = MiuixTheme.colorScheme.primary,
                    ),
                    shape = RoundedCornerShape(percent = 100),
                    border = BorderStroke(
                        width = 1.dp,
                        MiuixTheme.colorScheme.outline
                    )
                )
                FilterChip(
                    modifier = Modifier.weight(1f).height(48.dp),
                    label = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "Expense",
                            style = MiuixTheme.textStyles.headline2,
                            color = if (transactionType == TransactionType.EXPENSE) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.outline
                        )
                    },
                    selected = transactionType == TransactionType.EXPENSE,
                    onClick = { onTransactionTypeChange(TransactionType.EXPENSE) },
                    colors = FilterChipDefaults.filterChipColors().copy(
                        containerColor = MiuixTheme.colorScheme.surface,
                        selectedContainerColor = MiuixTheme.colorScheme.surface,
                        selectedLabelColor = MiuixTheme.colorScheme.primary,
                    ),
                    shape = RoundedCornerShape(percent = 100),
                    border = BorderStroke(
                        width = 1.dp,
                        MiuixTheme.colorScheme.outline
                    )
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColorsPrimary(),
                onClick = {
                    println("amount: $amount")
                    if (amount.isNaN() || amount <= 0.0 || amount > Double.MAX_VALUE) {
                        isAmountError = true
                        return@Button
                    }

                    isAmountError = false

                    onSubmitTransaction()
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MiuixTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = submitText, color = Color.White)
                }
            }
        }
    }
}