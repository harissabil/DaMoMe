package com.harissabil.damome.core.utils

import androidx.compose.ui.text.input.TextFieldValue
import java.text.DecimalFormatSymbols

/**
 * Sanitizes user input by removing unwanted characters.
 * The only allowed characters are
 * - any digit
 * - decimal separator
 * - grouping separator
 */
internal fun sanitizeInput(
    decimalFormatSymbols: DecimalFormatSymbols,
    input: TextFieldValue,
): TextFieldValue {

    // StringBuilder will remain null until we need to delete some characters
    var sb: StringBuilder? = null
    val inputText = input.text

    // Index of character either in input text or in StringBuilder if we have one
    var charIndex = 0

    fun onNotAllowed() {
        if (sb == null) {
            sb = StringBuilder(inputText)
        }

        // Delete current not allowed character
        if (charIndex >= 0) {
            sb!!.deleteCharAt(charIndex)
            charIndex--
        }

        if (charIndex < 0) {
            charIndex = 0
        }
    }

    while (charIndex >= 0 && charIndex < (sb?.length ?: inputText.length)) {
        val result = testCharacter(
            sb?.get(charIndex) ?: inputText[charIndex],
            decimalFormatSymbols,
        )

        when (result) {
            CharacterTestResult.NOT_ALLOWED -> onNotAllowed()

            CharacterTestResult.ALLOWED -> {
                charIndex++
            }

            CharacterTestResult.ALLOWED_CURRENCY_SYMBOL -> {
                charIndex++
            }

            CharacterTestResult.POSSIBLE_CURRENCY_SYMBOL_MATCH -> {
                // If possible currency symbol match was the last character then treat as
                // NOT_ALLOWED
                if (charIndex == (sb?.length ?: inputText.length) - 1) {
                    onNotAllowed()
                } else {
                    charIndex++
                }
            }
        }
    }

    // StringBuilder created means we removed some characters
    return if (sb != null) {
        input.copy(text = sb.toString())
    } else {
        input
    }
}

private fun testCharacter(
    c: Char,
    decimalFormatSymbols: DecimalFormatSymbols,
): CharacterTestResult {
    if (c.isDigit()
        || c == decimalFormatSymbols.decimalSeparator
        || c == decimalFormatSymbols.groupingSeparator
    ) {
        return CharacterTestResult.ALLOWED
    }

    return CharacterTestResult.NOT_ALLOWED
}

private enum class CharacterTestResult {
    NOT_ALLOWED,
    ALLOWED,
    ALLOWED_CURRENCY_SYMBOL,
    POSSIBLE_CURRENCY_SYMBOL_MATCH
}