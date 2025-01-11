package com.harissabil.damome.domain.repository

import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TransactionRepository {
    suspend fun createTransaction(transaction: Transaction): Result<Unit>
    suspend fun updateTransaction(transaction: Transaction): Result<Unit>
    suspend fun deleteTransaction(transaction: Transaction)
    fun getPerDateTransactions(localDate: LocalDate): Flow<List<Transaction>>
    suspend fun getAllTransactions(): Flow<List<Transaction>>
    fun getTotalBalance(): Flow<Double>
    fun getAllIncome(): Flow<List<Transaction>>
    fun getAllExpense(): Flow<List<Transaction>>
    suspend fun retrieveSimilarTransactions(
        textEmbedding: FloatArray,
        neighbors: Int,
        fromDate: LocalDate,
        toDate: LocalDate
    ): List<Transaction>
}