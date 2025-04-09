package com.harissabil.damome.core.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

actual fun formatCurrency(currency: Currency, value: Double): String {
  val locale = Locale.forLanguageTag(currency.languageTag)
//    return String.format(locale = locale, "%,.2f", value)
    val formatter = NumberFormat.getCurrencyInstance(locale)
    // format without currency symbol
    val formattedAmount = formatter.format(value).replace(Regex("[^0-9.,]"), "")
    return formattedAmount
}

/**
 * Converts a formatted currency string to a Double value based on the locale
 * @param amount The formatted currency string (e.g., "3.300.000,00" for IDR or "3,300,000.00" for USD)
 * @param currency The currency symbol to determine the locale
 * @return Double value of the amount or null if parsing fails
 */
actual fun parseFormattedAmount(amount: String, currency: Currency): Double? {
    val locale = getLocaleFromCurrency(currency)
    return try {
        val format =
            NumberFormat.getNumberInstance(Locale(locale.language, locale.region)) as DecimalFormat
        format.parse(amount)?.toDouble()
    } catch (e: Exception) {
        println("Error parsing amount: $amount - ${e.message}")
        null
    }
}

/**
 * Formats a double value to a localized currency string
 * @param amount The double value to format
 * @param currency The currency symbol to determine the locale
 * @return Formatted string according to the locale's currency format
 */
actual fun formatToLocalizedString(amount: Double, currency: Currency): String {
    val format = NumberFormat.getNumberInstance(Locale.forLanguageTag(currency.languageTag)) as DecimalFormat
    return format.format(amount)
}