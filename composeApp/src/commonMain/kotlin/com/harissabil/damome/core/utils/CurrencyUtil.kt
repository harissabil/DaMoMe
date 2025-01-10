package com.harissabil.damome.core.utils

import androidx.compose.ui.text.intl.Locale

expect fun formatCurrency(currency: String, value: Double): String

fun getLocaleFromCurrencySymbol(currency: String): Locale {
    return when (currency) {
        "USD" -> Locale(languageTag = "en-US")
        "IDR" -> Locale(languageTag = "id-ID")
        "SGD" -> Locale(languageTag = "en-SG")
        "VND" -> Locale(languageTag = "vi-VN")
        "KRW" -> Locale(languageTag = "ko-KR")
        "EUR" -> Locale(languageTag = "de-DE")
        "JPY" -> Locale(languageTag = "ja-JP")
        "CNY" -> Locale(languageTag = "zh-CN")
        "DKK" -> Locale(languageTag = "da-DK")
        else -> Locale.current
    }
}

/**
 * Converts a formatted currency string to a Double value based on the locale
 * @param amount The formatted currency string (e.g., "3.300.000,00" for IDR or "3,300,000.00" for USD)
 * @param currencySymbol The currency symbol to determine the locale
 * @return Double value of the amount or null if parsing fails
 */
expect fun parseFormattedAmount(amount: String, currencySymbol: String): Double?

/**
 * Formats a double value to a localized currency string
 * @param amount The double value to format
 * @param currencySymbol The currency symbol to determine the locale
 * @return Formatted string according to the locale's currency format
 */
expect fun formatToLocalizedString(amount: Double, currencySymbol: String): String