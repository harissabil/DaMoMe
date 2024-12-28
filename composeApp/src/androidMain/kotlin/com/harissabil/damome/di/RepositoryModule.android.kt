package com.harissabil.damome.di

import com.harissabil.damome.data.local.entity.CurrencyEntity
import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import com.harissabil.damome.data.local.entity.TransactionEntity
import com.harissabil.damome.data.local.object_box.ChatGroupObject
import com.harissabil.damome.data.local.object_box.ChatLocalStoreAndroid
import com.harissabil.damome.data.local.object_box.ChatMessageObject
import com.harissabil.damome.data.local.object_box.CurrencyLocalStoreAndroid
import com.harissabil.damome.data.local.object_box.CurrencyObject
import com.harissabil.damome.data.local.object_box.TransactionLocalStoreAndroid
import com.harissabil.damome.data.local.object_box.TransactionObject
import com.harissabil.damome.data.local.repository.ChatRepositoryImpl
import com.harissabil.damome.data.local.repository.CurrencyRepositoryImpl
import com.harissabil.damome.data.local.repository.TransactionRepositoryImpl
import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.domain.model.ChatMessage
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.repository.ChatRepository
import com.harissabil.damome.domain.repository.CurrencyRepository
import com.harissabil.damome.domain.repository.TransactionRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val sharedRepositoryModule = module {
    single<TransactionRepository> {
        TransactionRepositoryImpl(
            transactionLocalStore = get<TransactionLocalStoreAndroid<TransactionEntity>>(),
            entityDataSource = get<(transaction: Transaction) -> TransactionObject<out TransactionEntity>>(
                named("TransactionObject")
            ),
        )
    }
    single<CurrencyRepository> {
        CurrencyRepositoryImpl(
            currencyLocalStore = get<CurrencyLocalStoreAndroid<CurrencyEntity>>(),
            entityDataSource = get<(currency: String) -> CurrencyObject<out CurrencyEntity>>(named("CurrencyObject")),
        )
    }
    single<ChatRepository> {
        ChatRepositoryImpl(
            chatLocalStore = get<ChatLocalStoreAndroid>(),
            chatGroupEntityDataSource = get<(chatGroup: ChatGroup) -> ChatGroupObject<out IChatGroupEntity>>(
                named("ChatGroupObject")
            ),
            chatMessageEntityDataSource = get<(chatMessage: ChatMessage) -> ChatMessageObject<out IChatMessageEntity>>(
                named("ChatMessageObject")
            ),
            transactionEntityDataSource = get<(transaction: Transaction) -> TransactionObject<out TransactionEntity>>(
                named("TransactionObject")
            ),
        )
    }
}