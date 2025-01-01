package com.harissabil.damome.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harissabil.damome.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    // Insert or update a currency
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(currency: CurrencyEntity)

    // Get the first currency in the table
    @Query("SELECT * FROM CurrencyEntity LIMIT 1")
    suspend fun get(): CurrencyEntity?

    // Get all currencies as a Flow
    @Query("SELECT * FROM CurrencyEntity")
    fun getFlow(): Flow<List<CurrencyEntity>>
}
