package com.harissabil.damome.data.local.repository

import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import com.harissabil.damome.data.local.entity.ITransactionEntity
import com.harissabil.damome.data.local.object_box.ChatGroupObject
import com.harissabil.damome.data.local.object_box.ChatLocalStore
import com.harissabil.damome.data.local.object_box.ChatMessageObject
import com.harissabil.damome.data.local.object_box.TransactionObject
import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.domain.model.ChatMessage
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.toChatGroup
import com.harissabil.damome.domain.model.toChatMessage
import com.harissabil.damome.domain.model.toTransaction
import com.harissabil.damome.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val chatLocalStore: ChatLocalStore<out IChatGroupEntity, out IChatMessageEntity, out ITransactionEntity>,
    private val chatGroupEntityDataSource: (chatGroup: ChatGroup) -> ChatGroupObject<out IChatGroupEntity>,
    private val chatMessageEntityDataSource: (chatMessage: ChatMessage) -> ChatMessageObject<out IChatMessageEntity>,
    private val transactionEntityDataSource: (transaction: Transaction) -> TransactionObject<out ITransactionEntity>,
) : ChatRepository {
    override suspend fun saveChatGroup(chatGroup: ChatGroup): Long {
        return chatLocalStore.putChatGroup(chatGroupEntityDataSource(chatGroup))
    }

    override suspend fun saveChatMessage(chatGroupId: Long, messages: ChatMessage) {
        val relatedTransactions = messages.relatedTransactions
        if (!relatedTransactions.isNullOrEmpty()) {
            chatLocalStore.putChatMessageAndTransactionToChatGroup(
                chatGroupId = chatGroupId,
                chatMessages = chatMessageEntityDataSource(messages),
                transactions = relatedTransactions.map { transactionEntityDataSource(it) }
            )
        } else {
            chatLocalStore.putChatMessageToChatGroup(
                chatGroupId = chatGroupId,
                chatMessages = chatMessageEntityDataSource(messages)
            )
        }
    }

    override fun getChatHistory(): Flow<List<ChatGroup>> {
        return chatLocalStore.allChatGroup().map { chatGroups ->
            chatGroups.map { it.toChatGroup() }
        }
    }

    override suspend fun getChatMessage(chatGroupId: Long): List<ChatMessage> {
        return chatLocalStore.allChatMessage(chatGroupId).map { message ->
            val relatedTransactions = chatLocalStore.getTransactionByChatMessageId(message.id!!)
            if (relatedTransactions.isNotEmpty()) {
                message.toChatMessage()
                    .copy(relatedTransactions = relatedTransactions.map { it.toTransaction() })
            } else {
                message.toChatMessage()
            }
        }
    }

    override suspend fun deleteChatGroup(chatGroupId: Long) {
        chatLocalStore.deleteChatGroupAndMessage(chatGroupId)
    }
}