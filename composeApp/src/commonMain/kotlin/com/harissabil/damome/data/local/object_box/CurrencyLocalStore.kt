package com.harissabil.damome.data.local.object_box

import kotlinx.coroutines.flow.Flow

interface CurrencyLocalStore<T> {
    suspend fun <E> put(entity: CurrencyObject<E>)
    suspend fun get(): T?
    fun getFlow(): Flow<List<T>>
}