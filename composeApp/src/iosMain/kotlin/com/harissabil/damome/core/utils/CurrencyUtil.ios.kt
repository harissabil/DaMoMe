package com.harissabil.damome.core.utils

actual fun formatCurrency(currency: Currency, value: Double): String {
    TODO("Not yet implemented")
}

/**
 * Converts a formatted currency string to a Double value based on the locale
 * @param amount The formatted currency string (e.g., "3.300.000,00" for IDR or "3,300,000.00" for USD)
 * @param currency The currency symbol to determine the locale
 * @return Double value of the amount or null if parsing fails
 */
actual fun parseFormattedAmount(
    amount: String,
    currency: Currency,
): Double? {
    TODO("Not yet implemented")
}

/**
 * Formats a double value to a localized currency string
 * @param amount The double value to format
 * @param currency The currency symbol to determine the locale
 * @return Formatted string according to the locale's currency format
 */
actual fun formatToLocalizedString(
    amount: Double,
    currency: Currency,
): String {
    TODO("Not yet implemented")
}