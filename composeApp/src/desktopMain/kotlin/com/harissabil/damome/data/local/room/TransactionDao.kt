package com.harissabil.damome.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.harissabil.damome.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // Insert or update a transaction
    @Upsert
    suspend fun put(transaction: TransactionEntity)

    // Delete a transaction
    @Delete
    suspend fun delete(transaction: TransactionEntity)

    // Find transactions by date
    @Query(
        """
        SELECT * FROM TransactionEntity 
        WHERE timestamp BETWEEN :startTimestamp AND :endTimestamp
    """
    )
    fun findTransactionByDate(
        startTimestamp: Long,
        endTimestamp: Long,
    ): Flow<List<TransactionEntity>>

    // Get all transactions
    @Query("SELECT * FROM TransactionEntity")
    fun all(): Flow<List<TransactionEntity>>

    // Get all transactions of a specific type
    @Query("SELECT * FROM TransactionEntity WHERE type = :type")
    fun all(type: String): Flow<List<TransactionEntity>>

    // get all by description that matches the query you can undercase the query
    @Query("SELECT * FROM TransactionEntity WHERE description LIKE '%' || :description || '%'")
    suspend fun searchByDescription(description: String): List<TransactionEntity>

    // Find nearest neighbors (assuming embedding data is indexed in some way)
    @Query(
        """
        SELECT * FROM TransactionEntity 
        WHERE embedding  LIKE :queryVector BETWEEN :fromDate AND :toDate LIMIT :neighbors
    """
    )
    suspend fun findNearestNeighbors(
        queryVector: FloatArray,
        neighbors: Int,
        fromDate: Long,
        toDate: Long,
    ): List<TransactionEntity>
}
