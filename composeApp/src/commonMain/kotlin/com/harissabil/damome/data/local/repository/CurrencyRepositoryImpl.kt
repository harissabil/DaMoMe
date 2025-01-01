package com.harissabil.damome.data.local.repository

import com.harissabil.damome.data.local.entity.ICurrencyEntity
import com.harissabil.damome.data.local.object_box.CurrencyLocalStore
import com.harissabil.damome.data.local.object_box.CurrencyObject
import com.harissabil.damome.domain.model.Currency
import com.harissabil.damome.domain.model.toCurrency
import com.harissabil.damome.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyRepositoryImpl(
    private val currencyLocalStore: CurrencyLocalStore<out ICurrencyEntity>,
    private val entityDataSource: (currency: String) -> CurrencyObject<out ICurrencyEntity>,
) : CurrencyRepository {
    override suspend fun setCurrency(currency: String): com.harissabil.damome.core.utils.Result<Unit> {
        return try {
            currencyLocalStore.put(entityDataSource(currency))
            com.harissabil.damome.core.utils.Result.Success(Unit)
        } catch (e: Exception) {
            com.harissabil.damome.core.utils.Result.Error(
                message = e.message ?: "Something went wrong!"
            )
        }
    }

    override suspend fun getCurrency(): com.harissabil.damome.core.utils.Result<Currency?> {
        return try {
            val currency = currencyLocalStore.get()?.toCurrency()
            com.harissabil.damome.core.utils.Result.Success(currency)
        } catch (e: Exception) {
            com.harissabil.damome.core.utils.Result.Error(
                message = e.message ?: "Something went wrong!"
            )
        }
    }

    override fun getCurrencyFlow(): Flow<List<Currency>> {
        return currencyLocalStore.getFlow().map { list ->
            list.map { it.toCurrency() }
        }
    }
}