package com.harissabil.damome.data.local.object_box

import com.harissabil.damome.data.local.entity.ChatGroupEntity
import com.harissabil.damome.data.local.entity.ChatMessageEntity
import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import com.harissabil.damome.data.local.entity.ITransactionEntity
import com.harissabil.damome.data.local.entity.TransactionEntity
import io.objectbox.Box
import io.objectbox.kotlin.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

class ChatLocalStoreAndroid(
    chatGroupEntityClass: KClass<ChatGroupEntity>,
    chatMessageEntityClass: KClass<ChatMessageEntity>,
) : ChatLocalStore<ChatGroupEntity, ChatMessageEntity, TransactionEntity> {
    private val chatGroupEntityBox: Box<ChatGroupEntity> =
        ObjectBox.store.boxFor(chatGroupEntityClass.java)
    private val chatMessageEntityBox: Box<ChatMessageEntity> =
        ObjectBox.store.boxFor(chatMessageEntityClass.java)

    override suspend fun <E : IChatGroupEntity> putChatGroup(chatGroup: ChatGroupObject<E>): Long {
        val chatGroupEntity = chatGroup as ChatGroupObjectAndroid<ChatGroupEntity>
        return chatGroupEntityBox.put(chatGroupEntity.entity)
    }

    override suspend fun <V : IChatMessageEntity, X : ITransactionEntity> putChatMessageAndTransactionToChatGroup(
        chatGroupId: Long,
        chatMessages: ChatMessageObject<V>,
        transactions: List<TransactionObject<X>>,
    ) {
        val chatMessageEntity = chatMessages as ChatMessageObjectAndroid<ChatMessageEntity>
        val transactionEntity = transactions as List<TransactionObjectAndroid<TransactionEntity>>
        chatMessageEntity.entity.chatGroupId = chatGroupId
        chatMessageEntity.entity.transactions.addAll(transactionEntity.map { it.entity })
        val chatGroupToInsert = chatGroupEntityBox.get(chatGroupId)
        chatGroupToInsert.chatMessages.add(chatMessageEntity.entity)
        chatGroupEntityBox.put(chatGroupToInsert)
    }

    override suspend fun <V : IChatMessageEntity> putChatMessageToChatGroup(
        chatGroupId: Long,
        chatMessages: ChatMessageObject<V>,
    ) {
        val chatMessageEntity = chatMessages as ChatMessageObjectAndroid<ChatMessageEntity>
        val chatGroupToInsert = chatGroupEntityBox.get(chatGroupId)
        chatGroupToInsert.chatMessages.add(chatMessageEntity.entity)
        chatGroupEntityBox.put(chatGroupToInsert)
    }

    override suspend fun <E : IChatGroupEntity, V : IChatMessageEntity> putChatGroupAndMessage(
        chatGroup: ChatGroupObject<E>,
        chatMessages: List<ChatMessageObject<V>>,
    ) {
        val chatGroupEntity = chatGroup as ChatGroupObjectAndroid<ChatGroupEntity>
        val chatMessageEntity = chatMessages as List<ChatMessageObjectAndroid<ChatMessageEntity>>
        chatMessageEntity.forEach { message ->
            chatGroupEntity.entity.chatMessages.add(message.entity)
        }
        chatGroupEntityBox.put(chatGroupEntity.entity)
    }

    override suspend fun getTransactionByChatMessageId(chatMessageId: Long): List<TransactionEntity> {
        return chatMessageEntityBox.get(chatMessageId).transactions
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allChatGroup(): Flow<List<ChatGroupEntity>> {
        val query = chatGroupEntityBox.query().build()
        return query.subscribe().toFlow()
    }

    override suspend fun allChatMessage(chatGroupId: Long): List<ChatMessageEntity> {
        return chatGroupEntityBox.get(chatGroupId).chatMessages
    }

    override suspend fun deleteChatGroupAndMessage(chatGroupId: Long) {
        val chatMessages = chatGroupEntityBox.get(chatGroupId).chatMessages
        chatMessages.forEach { message ->
            chatMessageEntityBox.remove(message)
        }

        chatGroupEntityBox.remove(chatGroupId)
    }
}