package com.harissabil.damome.data.local.repository

import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.data.local.entity.ITransactionEntity
import com.harissabil.damome.data.local.object_box.TransactionLocalStore
import com.harissabil.damome.data.local.object_box.TransactionObject
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.toTransaction
import com.harissabil.damome.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class TransactionRepositoryImpl(
    private val transactionLocalStore: TransactionLocalStore<out ITransactionEntity>,
    private val entityDataSource: (transaction: Transaction) -> TransactionObject<out ITransactionEntity>,
) : TransactionRepository {
    override suspend fun createTransaction(transaction: Transaction): Result<Unit> {
        try {
            transactionLocalStore.put(entityDataSource(transaction))
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(message = e.message ?: "Something went wrong!")
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> {
        try {
            transactionLocalStore.put(entityDataSource(transaction))
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error(message = e.message ?: "Something went wrong!")
        }
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionLocalStore.delete(entityDataSource(transaction))
    }

    override fun getPerDateTransactions(localDate: LocalDate): Flow<List<Transaction>> {
        return transactionLocalStore.findTransactionByDate(localDate)
            .map { it.map(ITransactionEntity::toTransaction) }
    }

    override suspend fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionLocalStore.all().map { it.map(ITransactionEntity::toTransaction) }
    }

    override fun getTotalBalance(): Flow<Double> {
        return combine(
            transactionLocalStore.all(type = "income").map {
                it.sumOf(
                    ITransactionEntity::amount
                )
            },
            transactionLocalStore.all(type = "expense").map {
                it.sumOf(
                    ITransactionEntity::amount
                )
            }
        ) { income, expense ->
            println("income: $income, expense: $expense")
            income - expense
        }
    }

    override fun getAllIncome(): Flow<List<Transaction>> {
        return transactionLocalStore.all(type = "income")
            .map { it.map(ITransactionEntity::toTransaction) }
    }

    override fun getAllExpense(): Flow<List<Transaction>> {
        return transactionLocalStore.all(type = "expense")
            .map { it.map(ITransactionEntity::toTransaction) }
    }

    override suspend fun retrieveSimilarTransactions(
        textEmbedding: FloatArray,
        neighbors: Int,
        fromDate: LocalDate,
        toDate: LocalDate,
    ): List<Transaction> {
        return transactionLocalStore.findNearestNeighbors(
            textEmbedding,
            neighbors,
            fromDate,
            toDate
        )
            .map(ITransactionEntity::toTransaction)
    }
}