package com.harissabil.damome.data.local.repository

import com.harissabil.damome.data.local.entity.ICurrencyEntity
import com.harissabil.damome.data.local.object_box.CurrencyLocalStore
import com.harissabil.damome.data.local.object_box.CurrencyObject
import com.harissabil.damome.domain.model.Currency
import com.harissabil.damome.domain.model.toCurrency
import com.harissabil.damome.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val currencyLocalStore: CurrencyLocalStore<out ICurrencyEntity>,
    private val entityDataSource: (currency: String) -> CurrencyObject<out ICurrencyEntity>,
) : CurrencyRepository {
    override fun setCurrency(currency: String) {
        currencyLocalStore.put(entityDataSource(currency))
    }

    override fun getCurrency(): Currency {
        return currencyLocalStore.get().toCurrency()
    }
}