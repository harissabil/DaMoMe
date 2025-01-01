package com.harissabil.damome.data.local.local_store

import com.harissabil.damome.data.local.entity.ChatGroupEntity
import com.harissabil.damome.data.local.entity.ChatMessageEntity
import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import com.harissabil.damome.data.local.entity.ITransactionEntity
import com.harissabil.damome.data.local.entity.TransactionEntity
import com.harissabil.damome.data.local.object_box.ChatGroupObject
import com.harissabil.damome.data.local.object_box.ChatLocalStore
import com.harissabil.damome.data.local.object_box.ChatMessageObject
import com.harissabil.damome.data.local.object_box.TransactionObject
import com.harissabil.damome.data.local.room.ChatDao
import kotlinx.coroutines.flow.Flow

class ChatLocalStoreDesktop(
    private val chatDao: ChatDao,
) : ChatLocalStore<ChatGroupEntity, ChatMessageEntity, TransactionEntity> {

    override suspend fun <E : IChatGroupEntity> putChatGroup(chatGroup: ChatGroupObject<E>): Long {
        val chatGroupEntity = chatGroup as ChatGroupObjectDesktop<ChatGroupEntity>
        return chatDao.putChatGroup(chatGroupEntity.entity)
    }

    override suspend fun <V : IChatMessageEntity, X : ITransactionEntity> putChatMessageAndTransactionToChatGroup(
        chatGroupId: Long,
        chatMessages: ChatMessageObject<V>,
        transactions: List<TransactionObject<X>>,
    ) {
        val chatMessageEntity = chatMessages as ChatMessageObjectDesktop<ChatMessageEntity>
        val transactionEntity = transactions as List<TransactionObjectDesktop<TransactionEntity>>
        chatMessageEntity.entity.chatGroupId = chatGroupId
        chatDao.putChatMessageAndTransactionToChatGroup(
            chatGroupId,
            chatMessageEntity.entity,
            transactionEntity.map { it.entity })
    }

    override suspend fun <V : IChatMessageEntity> putChatMessageToChatGroup(
        chatGroupId: Long,
        chatMessages: ChatMessageObject<V>,
    ) {
        val chatMessageEntity = chatMessages as ChatMessageObjectDesktop<ChatMessageEntity>
        chatMessageEntity.entity.chatGroupId = chatGroupId
        chatDao.putChatMessage(chatMessageEntity.entity)
    }

    override suspend fun <E : IChatGroupEntity, V : IChatMessageEntity> putChatGroupAndMessage(
        chatGroup: ChatGroupObject<E>,
        chatMessages: List<ChatMessageObject<V>>,
    ) {
        val chatGroupEntity = chatGroup as ChatGroupObjectDesktop<ChatGroupEntity>
        val chatMessageEntities = chatMessages as List<ChatMessageObjectDesktop<ChatMessageEntity>>
        val chatGroupId = chatDao.putChatGroup(chatGroupEntity.entity)
        chatMessageEntities.forEach { chatMessage ->
            chatMessage.entity.chatGroupId = chatGroupId
            chatDao.putChatMessage(chatMessage.entity)
        }
    }

    override suspend fun getTransactionByChatMessageId(chatMessageId: Long): List<TransactionEntity> {
        return chatDao.getTransactionByChatMessageId(chatMessageId)
    }

    override fun allChatGroup(): Flow<List<ChatGroupEntity>> {
        return chatDao.allChatGroup()
    }

    override suspend fun allChatMessage(chatGroupId: Long): List<ChatMessageEntity> {
        return chatDao.allChatMessage(chatGroupId)
    }

    override suspend fun deleteChatGroupAndMessage(chatGroupId: Long) {
        chatDao.deleteChatGroupAndMessage(chatGroupId)
    }
}