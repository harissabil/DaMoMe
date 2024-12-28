package com.harissabil.damome.domain.repository

import com.harissabil.damome.domain.model.Currency

interface CurrencyRepository {
    fun setCurrency(currency: String)
    fun getCurrency(): Currency
}