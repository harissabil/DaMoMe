package com.harissabil.damome.data.local.object_box

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TransactionLocalStore<T> {
    suspend fun <E> put(entity: TransactionObject<E>)

    suspend fun <E> delete(entity: TransactionObject<E>)

    fun findTransactionByDate(localDate: LocalDate): Flow<List<T>>

    suspend fun all(): Flow<List<T>>

    fun all(type: String): Flow<List<T>>

    suspend fun findNearestNeighbors(
        queryVector: FloatArray,
        neighbors: Int,
        fromDate: LocalDate,
        toDate: LocalDate
    ): List<T>
}