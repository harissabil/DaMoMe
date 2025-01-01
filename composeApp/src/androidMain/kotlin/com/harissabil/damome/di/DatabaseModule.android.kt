package com.harissabil.damome.di

import com.harissabil.damome.data.local.entity.ChatGroupEntity
import com.harissabil.damome.data.local.entity.ChatMessageEntity
import com.harissabil.damome.data.local.entity.CurrencyEntity
import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import com.harissabil.damome.data.local.entity.TransactionEntity
import com.harissabil.damome.data.local.entity.TransactionEntity_
import com.harissabil.damome.data.local.object_box.ChatGroupObject
import com.harissabil.damome.data.local.object_box.ChatGroupObjectAndroid
import com.harissabil.damome.data.local.object_box.ChatLocalStoreAndroid
import com.harissabil.damome.data.local.object_box.ChatMessageObject
import com.harissabil.damome.data.local.object_box.ChatMessageObjectAndroid
import com.harissabil.damome.data.local.object_box.CurrencyLocalStoreAndroid
import com.harissabil.damome.data.local.object_box.CurrencyObject
import com.harissabil.damome.data.local.object_box.CurrencyObjectAndroid
import com.harissabil.damome.data.local.object_box.TransactionLocalStoreAndroid
import com.harissabil.damome.data.local.object_box.TransactionObject
import com.harissabil.damome.data.local.object_box.TransactionObjectAndroid
import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.domain.model.ChatMessage
import com.harissabil.damome.domain.model.Transaction
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val databaseModule = module {
    // Transaction
    single {
        TransactionLocalStoreAndroid(
            entityClass = TransactionEntity::class,
            typeProperty = TransactionEntity_.type,
            timestampProperty = TransactionEntity_.timestamp,
            embeddingProperty = TransactionEntity_.embedding
        )
    }

    single<(transaction: Transaction) -> TransactionObject<out TransactionEntity>>(named("TransactionObject")) {
        { transaction ->
            TransactionObjectAndroid(
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
        CurrencyLocalStoreAndroid(
            entityClass = CurrencyEntity::class
        )
    }

    single<(currency: String) -> CurrencyObject<out CurrencyEntity>>(named("CurrencyObject")) {
        { currency ->
            CurrencyObjectAndroid(
                box = CurrencyEntity(
                    id = null,
                    currency = currency
                )
            )
        }
    }

    // Chat
    single {
        ChatLocalStoreAndroid(
            chatGroupEntityClass = ChatGroupEntity::class,
            chatMessageEntityClass = ChatMessageEntity::class
        )
    }

    single<(chatGroup: ChatGroup) -> ChatGroupObject<out IChatGroupEntity>>(named("ChatGroupObject")) {
        { chatGroup ->
            ChatGroupObjectAndroid(
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
            ChatMessageObjectAndroid(
                box = ChatMessageEntity(
                    id = chatMessage.id,
                    chatGroupId = chatMessage.chatGroupId,
                    timestamp = chatMessage.timestamp,
                    isUser = chatMessage.isUser,
                    order = chatMessage.order,
                    message = chatMessage.message
                )
            )
        }
    }
}