package com.harissabil.damome.core.utils

import androidx.compose.ui.text.intl.Locale

expect fun formatCurrency(currency: String, value: Double): String

fun getLocaleFromCurrencySymbol(currency: String): Locale {
    return when (currency) {
        "IDR" -> Locale(languageTag = "id-ID") // Indonesia
        "USD" -> Locale(languageTag = "en-US") // United States
        "EUR" -> Locale(languageTag = "de-DE") // Germany (default for Euro)
        "JPY" -> Locale(languageTag = "ja-JP") // Japan
        "KRW" -> Locale(languageTag = "ko-KR") // South Korea
        "CNY" -> Locale(languageTag = "zh-CN") // China
        "SGD" -> Locale(languageTag = "en-SG") // Singapore
        "VND" -> Locale(languageTag = "vi-VN") // Vietnam
        "DKK" -> Locale(languageTag = "da-DK") // Denmark
        "AUD" -> Locale(languageTag = "en-AU") // Australia
        "BRL" -> Locale(languageTag = "pt-BR") // Brazil
        "CAD" -> Locale(languageTag = "en-CA") // Canada
        "CHF" -> Locale(languageTag = "de-CH") // Switzerland
        "GBP" -> Locale(languageTag = "en-GB") // United Kingdom
        "HKD" -> Locale(languageTag = "zh-HK") // Hong Kong
        "INR" -> Locale(languageTag = "hi-IN") // India
        "MXN" -> Locale(languageTag = "es-MX") // Mexico
        "MYR" -> Locale(languageTag = "ms-MY") // Malaysia
        "NOK" -> Locale(languageTag = "nb-NO") // Norway
        "NZD" -> Locale(languageTag = "en-NZ") // New Zealand
        "PHP" -> Locale(languageTag = "fil-PH") // Philippines
        "RUB" -> Locale(languageTag = "ru-RU") // Russia
        "SAR" -> Locale(languageTag = "ar-SA") // Saudi Arabia
        "SEK" -> Locale(languageTag = "sv-SE") // Sweden
        "THB" -> Locale(languageTag = "th-TH") // Thailand
        "TRY" -> Locale(languageTag = "tr-TR") // Turkey
        "ZAR" -> Locale(languageTag = "en-ZA") // South Africa
        else -> Locale.current // Default to current locale
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