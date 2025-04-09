package com.harissabil.damome.core.utils

import androidx.compose.ui.text.intl.Locale


enum class Currency(val symbol: String, val languageTag: String) {
    IDR("IDR", "id-ID"), // Indonesia
    USD("USD", "en-US"), // United States
    EUR("EUR", "de-DE"), // Germany (default for Euro)
    JPY("JPY", "ja-JP"), // Japan
    KRW("KRW", "ko-KR"), // South Korea
    CNY("CNY", "zh-CN"), // China
    SGD("SGD", "en-SG"), // Singapore
    VND("VND", "vi-VN"), // Vietnam
    DKK("DKK", "da-DK"), // Denmark
    AUD("AUD", "en-AU"), // Australia
    BRL("BRL", "pt-BR"), // Brazil
    CAD("CAD", "en-CA"), // Canada
    CHF("CHF", "de-CH"), // Switzerland
    GBP("GBP", "en-GB"), // United Kingdom
    HKD("HKD", "zh-HK"), // Hong Kong
    INR("INR", "hi-IN"), // India
    MXN("MXN", "es-MX"), // Mexico
    MYR("MYR", "ms-MY"), // Malaysia
    NOK("NOK", "nb-NO"), // Norway
    NZD("NZD", "en-NZ"), // New Zealand
    PHP("PHP", "fil-PH"), // Philippines
    RUB("RUB", "ru-RU"), // Russia
    SAR("SAR", "ar-SA"), // Saudi Arabia
    SEK("SEK", "sv-SE"), // Sweden
    THB("THB", "th-TH"), // Thailand
    TRY("TRY", "tr-TR"), // Turkey
    ZAR("ZAR", "en-ZA"), // South Africa
    DZD("DZD", "ar-DZ"), // Algeria ,
    EMPTY("", ""),
}

fun List<Currency>.getSymbolsList(): List<String> {
    return Currency.entries.mapNotNull { innerCurrency ->
        // cause the EMPTY currency is not a valid currency
        innerCurrency.symbol.takeIf { it.isNotEmpty() }
    }
}


fun com.harissabil.damome.domain.model.Currency.toCurrency(): Currency {
    var currency: Currency? = null
    Currency.entries.forEach { innerCurrency ->
        if (this.currency == innerCurrency.symbol) {
            currency = innerCurrency
        }
    }
    return currency ?: Currency.EMPTY
}


expect fun formatCurrency(currency: Currency, value: Double): String

fun getLocaleFromCurrency(currency: Currency): Locale {
    return Locale(languageTag = currency.languageTag)
}


/**
 * Converts a formatted currency string to a Double value based on the locale
 * @param amount The formatted currency string (e.g., "3.300.000,00" for IDR or "3,300,000.00" for USD)
 * @param currency The currency symbol to determine the locale
 * @return Double value of the amount or null if parsing fails
 */
expect fun parseFormattedAmount(amount: String, currency: Currency): Double?

/**
 * Formats a double value to a localized currency string
 * @param amount The double value to format
 * @param currency The currency symbol to determine the locale
 * @return Formatted string according to the locale's currency format
 */
expect fun formatToLocalizedString(amount: Double, currency: Currency): String