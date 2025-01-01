package com.harissabil.damome.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.harissabil.damome.data.local.entity.ChatGroupEntity
import com.harissabil.damome.data.local.entity.ChatMessageEntity
import com.harissabil.damome.data.local.entity.CurrencyEntity
import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import com.harissabil.damome.data.local.entity.TransactionEntity
import com.harissabil.damome.data.local.local_store.ChatGroupObjectDesktop
import com.harissabil.damome.data.local.local_store.ChatLocalStoreDesktop
import com.harissabil.damome.data.local.local_store.ChatMessageObjectDesktop
import com.harissabil.damome.data.local.local_store.CurrencyLocalStoreDesktop
import com.harissabil.damome.data.local.local_store.CurrencyObjectDesktop
import com.harissabil.damome.data.local.local_store.TransactionLocalStoreDesktop
import com.harissabil.damome.data.local.local_store.TransactionObjectDesktop
import com.harissabil.damome.data.local.object_box.ChatGroupObject
import com.harissabil.damome.data.local.object_box.ChatMessageObject
import com.harissabil.damome.data.local.object_box.CurrencyObject
import com.harissabil.damome.data.local.object_box.TransactionObject
import com.harissabil.damome.data.local.room.DamomeDatabase
import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.domain.model.ChatMessage
import com.harissabil.damome.domain.model.Transaction
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

fun provideDamomeDatabase(): DamomeDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "damome_db")
    return Room.databaseBuilder<DamomeDatabase>(
        name = dbFile.absolutePath
    )
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .build()
}

fun provideChatDao(database: DamomeDatabase) = database.chatDao()
fun provideCurrencyDao(database: DamomeDatabase) = database.currencyDao()
fun provideTransactionDao(database: DamomeDatabase) = database.transactionDao()

actual val databaseModule = module {

    single { provideDamomeDatabase() }
    single { provideChatDao(get()) }
    single { provideCurrencyDao(get()) }
    single { provideTransactionDao(get()) }

    // Transaction
    single {
        TransactionLocalStoreDesktop(
            transactionDao = get()
        )
    }

    single<(transaction: Transaction) -> TransactionObject<out TransactionEntity>>(named("TransactionObject")) {
        { transaction ->
            TransactionObjectDesktop(
                box = TransactionEntity(
                    id = transaction.id,
                    type = transaction.type.value,
                    timestamp = transaction.timestamp,
                    amount = transaction.amount,
                    currency = transaction.currency,
                    category = transaction.category,
                    description = transaction.description,
                    textToEmbed = transaction.textToEmbed,
                    embedding = transaction.embedding
                )
            )
        }
    }

    // Currency
    single {
        CurrencyLocalStoreDesktop(
            currencyDao = get()
        )
    }

    single<(currency: String) -> CurrencyObject<out CurrencyEntity>>(named("CurrencyObject")) {
        { currency ->
            CurrencyObjectDesktop(
                box = CurrencyEntity(
                    id = null,
                    currency = currency
                )
            )
        }
    }

    // Chat
    single {
        ChatLocalStoreDesktop(
            chatDao = get()
        )
    }

    single<(chatGroup: ChatGroup) -> ChatGroupObject<out IChatGroupEntity>>(named("ChatGroupObject")) {
        { chatGroup ->
            ChatGroupObjectDesktop(
                box = ChatGroupEntity(
                    id = chatGroup.id,
                    name = chatGroup.name,
                    timestamp = chatGroup.timestamp
                )
            )
        }
    }

    single<(chatMessage: ChatMessage) -> ChatMessageObject<out IChatMessageEntity>>(named("ChatMessageObject")) {
        { chatMessage ->
            ChatMessageObjectDesktop(
                box = ChatMessageEntity(
                    id = chatMessage.id,
                    timestamp = chatMessage.timestamp,
                    isUser = chatMessage.isUser,
                    order = chatMessage.order,
                    message = chatMessage.message,
                    chatGroupId = chatMessage.chatGroupId
                )
            )
        }
    }
}