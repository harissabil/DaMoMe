package com.harissabil.damome.data.local.local_store

import com.harissabil.damome.data.local.entity.TransactionEntity
import com.harissabil.damome.data.local.object_box.TransactionLocalStore
import com.harissabil.damome.data.local.object_box.TransactionObject
import com.harissabil.damome.data.local.room.TransactionDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant

class TransactionLocalStoreDesktop(
    private val transactionDao: TransactionDao,
) : TransactionLocalStore<TransactionEntity> {

    override suspend fun <E> put(entity: TransactionObject<E>) {
        val androidEntity = entity as TransactionObjectDesktop<TransactionEntity>
        transactionDao.put(androidEntity.entity)
    }

    override suspend fun <E> delete(entity: TransactionObject<E>) {
        val androidEntity = entity as TransactionObjectDesktop<TransactionEntity>
        transactionDao.delete(androidEntity.entity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findTransactionByDate(localDate: LocalDate): Flow<List<TransactionEntity>> {
        val tz = TimeZone.currentSystemDefault()
        val startInstant = localDate.atStartOfDayIn(tz)
        val endInstant = localDate.atTime(23, 59, 59).toInstant(tz)

        val query = transactionDao.findTransactionByDate(
            startInstant.toEpochMilliseconds(),
            endInstant.toEpochMilliseconds()
        )

        return query
    }

    override suspend fun all(): List<TransactionEntity> {
        return transactionDao.all()
    }

    override fun all(type: String): Flow<List<TransactionEntity>> {
        return transactionDao.all(type)
    }

    override suspend fun findNearestNeighbors(
        queryVector: FloatArray,
        neighbors: Int,
    ): List<TransactionEntity> {
        return transactionDao.findNearestNeighbors(queryVector, neighbors)
    }
}