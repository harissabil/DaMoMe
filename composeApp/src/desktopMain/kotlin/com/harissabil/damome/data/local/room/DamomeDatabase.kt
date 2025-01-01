package com.harissabil.damome.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harissabil.damome.data.local.entity.ChatGroupEntity
import com.harissabil.damome.data.local.entity.ChatMessageEntity
import com.harissabil.damome.data.local.entity.ChatMessageTransactionCrossRef
import com.harissabil.damome.data.local.entity.CurrencyEntity
import com.harissabil.damome.data.local.entity.TransactionEntity

@Database(
    entities = [
        ChatGroupEntity::class,
        ChatMessageEntity::class,
        ChatMessageTransactionCrossRef::class,
        CurrencyEntity::class,
        TransactionEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class DamomeDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun transactionDao(): TransactionDao
}