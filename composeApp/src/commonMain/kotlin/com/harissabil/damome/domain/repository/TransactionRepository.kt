package com.harissabil.damome.domain.repository

import com.harissabil.damome.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun createTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    fun getTodayTransactions(): Flow<List<Transaction>>
    suspend fun getAllTransactions(): List<Transaction>
    suspend fun retrieveSimilarTransactions(
        textEmbedding: FloatArray,
        neighbors: Int,
    ): List<Transaction>
}