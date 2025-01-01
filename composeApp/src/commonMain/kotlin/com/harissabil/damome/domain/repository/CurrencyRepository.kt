package com.harissabil.damome.domain.repository

import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun setCurrency(currency: String): Result<Unit>
    suspend fun getCurrency(): Result<Currency?>
    fun getCurrencyFlow(): Flow<List<Currency>>
}