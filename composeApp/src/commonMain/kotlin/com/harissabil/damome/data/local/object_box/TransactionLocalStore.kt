package com.harissabil.damome.data.local.object_box

import kotlinx.coroutines.flow.Flow

interface TransactionLocalStore<T> {
    fun <E> put(entity: TransactionObject<E>)

    fun <E> delete(entity: TransactionObject<E>)

    fun today(): Flow<List<T>>

    fun all(): List<T>

    fun findNearestNeighbors(queryVector: FloatArray, neighbors: Int): List<T>
}