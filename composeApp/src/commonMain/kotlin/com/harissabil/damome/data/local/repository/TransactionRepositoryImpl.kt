package com.harissabil.damome.data.local.repository

import com.harissabil.damome.data.local.entity.ITransactionEntity
import com.harissabil.damome.data.local.object_box.TransactionLocalStore
import com.harissabil.damome.data.local.object_box.TransactionObject
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.toTransaction
import com.harissabil.damome.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionLocalStore: TransactionLocalStore<out ITransactionEntity>,
    private val entityDataSource: (transaction: Transaction) -> TransactionObject<out ITransactionEntity>,
) : TransactionRepository {
    override suspend fun createTransaction(transaction: Transaction) {
        transactionLocalStore.put(entityDataSource(transaction))
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionLocalStore.delete(entityDataSource(transaction))
    }

    override fun getTodayTransactions(): Flow<List<Transaction>> =
        transactionLocalStore.today().map { it.map(ITransactionEntity::toTransaction) }

    override suspend fun getAllTransactions(): List<Transaction> {
        return transactionLocalStore.all().map(ITransactionEntity::toTransaction)
    }

    override suspend fun retrieveSimilarTransactions(
        textEmbedding: FloatArray,
        neighbors: Int,
    ): List<Transaction> {
        return transactionLocalStore.findNearestNeighbors(textEmbedding, neighbors)
            .map(ITransactionEntity::toTransaction)
    }
}