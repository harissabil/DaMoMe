package com.harissabil.damome.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.harissabil.damome.core.utils.Currency

@Composable
actual fun CurrencyTextField(
    onChange: (String) -> Unit,
    modifier: Modifier,
    initialText: String,
    scannedAmount: String?,
    keyboardOptions: KeyboardOptions,
    maxLines: Int,
    maxNoOfDecimal: Int,
    currency: Currency,
) {

}