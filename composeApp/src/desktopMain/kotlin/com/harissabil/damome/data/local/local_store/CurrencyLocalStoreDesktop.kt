package com.harissabil.damome.data.local.local_store

import com.harissabil.damome.data.local.entity.CurrencyEntity
import com.harissabil.damome.data.local.object_box.CurrencyLocalStore
import com.harissabil.damome.data.local.object_box.CurrencyObject
import com.harissabil.damome.data.local.room.CurrencyDao
import kotlinx.coroutines.flow.Flow

class CurrencyLocalStoreDesktop(
    private val currencyDao: CurrencyDao,
) : CurrencyLocalStore<CurrencyEntity> {

    override suspend fun <E> put(entity: CurrencyObject<E>) {
        val androidEntity = entity as CurrencyObjectDesktop<CurrencyEntity>
        currencyDao.put(androidEntity.entity)
    }

    override suspend fun get(): CurrencyEntity? {
        return currencyDao.get()
    }

    override fun getFlow(): Flow<List<CurrencyEntity>> {
        return currencyDao.getFlow()
    }
}