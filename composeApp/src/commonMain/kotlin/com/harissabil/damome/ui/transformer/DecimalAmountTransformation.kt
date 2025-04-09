package com.harissabil.damome.ui.transformer

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.core.utils.formatCurrency

class DecimalAmountTransformation(
    private val currency: Currency,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val sanitizedText = originalText.replace(Regex("[^0-9.]"), "")
        val value = sanitizedText.toDoubleOrNull() ?: 0.0
        val formattedText = if (value == 0.0) {
            ""
        } else {
            formatCurrency(currency, value)
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formattedText.length
            override fun transformedToOriginal(offset: Int): Int = sanitizedText.length
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}