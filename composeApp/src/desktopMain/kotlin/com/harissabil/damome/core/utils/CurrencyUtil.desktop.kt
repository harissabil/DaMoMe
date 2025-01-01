package com.harissabil.damome.core.utils

import java.text.NumberFormat
import java.util.Locale

actual fun formatCurrency(currency: String, value: Double): String {
    val locale = when (currency) {
        "USD" -> Locale.US
        "IDR" -> Locale("id", "ID")
        "VND" -> Locale("vi", "VN")
        "KRW" -> Locale.KOREA
        "EUR" -> Locale.GERMANY
        "JPY" -> Locale.JAPAN
        "CNY" -> Locale.CHINA
        else -> Locale.US
    }
//    return String.format(locale = locale, "%,.2f", value)
    val formatter = NumberFormat.getCurrencyInstance(locale)
    // format without currency symbol
    val formattedAmount = formatter.format(value).replace(Regex("[^0-9.,]"), "")
    return formattedAmount
}