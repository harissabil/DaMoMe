package com.harissabil.damome.core.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

actual fun formatCurrency(currency: String, value: Double): String {
    val locale = when (currency) {
        "IDR" -> Locale("id", "ID") // Indonesia
        "USD" -> Locale.US // United States
        "EUR" -> Locale.GERMANY // Germany (default for Euro)
        "JPY" -> Locale.JAPAN // Japan
        "KRW" -> Locale.KOREA // South Korea
        "CNY" -> Locale.CHINA // China
        "SGD" -> Locale("en", "SG") // Singapore
        "VND" -> Locale("vi", "VN") // Vietnam
        "DKK" -> Locale("da", "DK") // Denmark
        "AUD" -> Locale("en", "AU") // Australia
        "BRL" -> Locale("pt", "BR") // Brazil
        "CAD" -> Locale("en", "CA") // Canada
        "CHF" -> Locale("de", "CH") // Switzerland
        "GBP" -> Locale.UK // United Kingdom
        "HKD" -> Locale("zh", "HK") // Hong Kong
        "INR" -> Locale("hi", "IN") // India
        "MXN" -> Locale("es", "MX") // Mexico
        "MYR" -> Locale("ms", "MY") // Malaysia
        "NOK" -> Locale("nb", "NO") // Norway
        "NZD" -> Locale("en", "NZ") // New Zealand
        "PHP" -> Locale("fil", "PH") // Philippines
        "RUB" -> Locale("ru", "RU") // Russia
        "SAR" -> Locale("ar", "SA") // Saudi Arabia
        "SEK" -> Locale("sv", "SE") // Sweden
        "THB" -> Locale("th", "TH") // Thailand
        "TRY" -> Locale("tr", "TR") // Turkey
        "ZAR" -> Locale("en", "ZA") // South Africa
        else -> Locale.US // Default fallback
    }
//    return String.format(locale = locale, "%,.2f", value)
    val formatter = NumberFormat.getCurrencyInstance(locale)
    // format without currency symbol
    val formattedAmount = formatter.format(value).replace(Regex("[^0-9.,]"), "")
    return formattedAmount
}

/**
 * Converts a formatted currency string to a Double value based on the locale
 * @param amount The formatted currency string (e.g., "3.300.000,00" for IDR or "3,300,000.00" for USD)
 * @param currencySymbol The currency symbol to determine the locale
 * @return Double value of the amount or null if parsing fails
 */
actual fun parseFormattedAmount(amount: String, currencySymbol: String): Double? {
    val locale = getLocaleFromCurrencySymbol(currencySymbol)
    return try {
        val format =
            NumberFormat.getNumberInstance(Locale(locale.language, locale.region)) as DecimalFormat
        format.parse(amount)?.toDouble()
    } catch (e: Exception) {
        null
    }
}

/**
 * Formats a double value to a localized currency string
 * @param amount The double value to format
 * @param currencySymbol The currency symbol to determine the locale
 * @return Formatted string according to the locale's currency format
 */
actual fun formatToLocalizedString(amount: Double, currencySymbol: String): String {
    val locale = getLocaleFromCurrencySymbol(currencySymbol)
    val format =
        NumberFormat.getNumberInstance(Locale(locale.language, locale.region)) as DecimalFormat
    return format.format(amount)
}