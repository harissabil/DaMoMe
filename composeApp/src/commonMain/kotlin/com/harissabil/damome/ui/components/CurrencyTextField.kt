package com.harissabil.damome.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.harissabil.damome.core.utils.Currency

@Composable
expect fun CurrencyTextField(
    onChange: ((String) -> Unit),
    modifier: Modifier = Modifier,
    initialText: String = "",
    scannedAmount: String? = null,
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Number),
    maxLines: Int = 1,
    maxNoOfDecimal: Int = 2,
    currency: Currency,
)