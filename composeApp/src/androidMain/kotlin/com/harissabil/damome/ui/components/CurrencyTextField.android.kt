package com.harissabil.damome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.core.utils.getLocaleFromCurrency
import com.harissabil.damome.core.utils.sanitizeInput
import top.yukonga.miuix.kmp.basic.TextField
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale
import kotlin.math.min

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
    var textFieldState by remember(scannedAmount) {
        mutableStateOf(
            TextFieldValue(
                text = scannedAmount ?: initialText
            )
        )
    }

    val locale = getLocaleFromCurrency(currency)

    val decimalFormatter: DecimalFormat =
        (NumberFormat.getNumberInstance(Locale(locale.language, locale.region)) as DecimalFormat)
            .apply {
                isDecimalSeparatorAlwaysShown = true
            }

    val decimalFormatSymbols: DecimalFormatSymbols =
        decimalFormatter.decimalFormatSymbols

    var oldText = ""

    Column(
        horizontalAlignment = Alignment.End
    ) {
        TextField(
            value = textFieldState,
            modifier = modifier,
            onValueChange = { value: TextFieldValue ->
                textFieldState = formatUserInput(
                    oldText,
                    sanitizeInput(
                        decimalFormatSymbols,
                        value
                    ),
                    decimalFormatSymbols,
                    maxNoOfDecimal,
                    decimalFormatter
                )
                oldText = textFieldState.text
                onChange(oldText)
            },
            keyboardOptions = keyboardOptions,
            maxLines = maxLines,
            singleLine = true,
        )
    }

}

private fun formatUserInput(
    oldText: String,
    textFieldValue: TextFieldValue,
    decimalFormatSymbols: DecimalFormatSymbols,
    maxNoOfDecimal: Int,
    decimalFormatter: DecimalFormat,
): TextFieldValue {
    if (oldText == textFieldValue.text)
        return TextFieldValue(
            text = oldText,
            selection = TextRange(oldText.length)
        )

    var userInput = textFieldValue.text
    var finalSelection = 0

    if (userInput.last().toString() == "." &&
        decimalFormatSymbols.decimalSeparator.toString() != userInput.last().toString()
    ) {
        userInput = userInput.dropLast(1)
        userInput.plus(decimalFormatSymbols.decimalSeparator.toString())
    }

    if (checkDecimalSizeExceeded(
            userInput,
            decimalFormatSymbols,
            maxNoOfDecimal
        ).not()
    ) {

        val startLength = textFieldValue.text.length

        try {
            val parsedNumber = decimalFormatter.parse(userInput)
            decimalFormatter.applyPattern(
                setDecimalFormatterSensitivity(
                    userInput, decimalFormatSymbols, maxNoOfDecimal
                )
            )

            val startPoint = textFieldValue.selection.start
            userInput = decimalFormatter.format(parsedNumber)
            val finalLength = userInput.length
            val selection = startPoint + (finalLength - startLength)

            finalSelection = if (selection > 0 && selection <= userInput.length) {
                selection
            } else {
                userInput.length - 1
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    } else {
        finalSelection = userInput.length - 1
        userInput = userInput.substring(0, userInput.length - 1)
    }

    return TextFieldValue(
        text = userInput,
        selection = TextRange(finalSelection)
    )
}

private fun setDecimalFormatterSensitivity(
    userInput: String,
    decimalFormatSymbols: DecimalFormatSymbols,
    maxNoOfDecimal: Int,
): String {

    val decimalSeparatorIndex = userInput.indexOf(decimalFormatSymbols.decimalSeparator)
    if (decimalSeparatorIndex == -1)
        return "#,##0"

    val noOfCharactersAfterDecimalPoint =
        userInput.length - decimalSeparatorIndex - 1

    val zeros = "0".repeat(
        min(
            noOfCharactersAfterDecimalPoint,
            maxNoOfDecimal
        )
    )
    return "#,##0.$zeros"

}

private fun checkDecimalSizeExceeded(
    input: String,
    decimalFormatSymbols: DecimalFormatSymbols,
    maxNoOfDecimal: Int,
): Boolean {
    return (input.split(decimalFormatSymbols.decimalSeparator)
        .getOrNull(1)?.length ?: Int.MIN_VALUE) > maxNoOfDecimal
}