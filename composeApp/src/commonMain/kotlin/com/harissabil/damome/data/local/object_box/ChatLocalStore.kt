package com.harissabil.damome.data.local.object_box

import com.harissabil.damome.data.local.entity.IChatGroupEntity
import com.harissabil.damome.data.local.entity.IChatMessageEntity
import com.harissabil.damome.data.local.entity.ITransactionEntity
import kotlinx.coroutines.flow.Flow

interface ChatLocalStore<T, U, W> {
    suspend fun <E : IChatGroupEntity> putChatGroup(chatGroup: ChatGroupObject<E>): Long

    suspend fun <V : IChatMessageEntity, X : ITransactionEntity> putChatMessageAndTransactionToChatGroup(
        chatGroupId: Long,
        chatMessages: ChatMessageObject<V>,
        transactions: List<TransactionObject<X>>,
    )

    suspend fun <V : IChatMessageEntity> putChatMessageToChatGroup(
        chatGroupId: Long,
        chatMessages: ChatMessageObject<V>,
    )

    suspend fun <E : IChatGroupEntity, V : IChatMessageEntity> putChatGroupAndMessage(
        chatGroup: ChatGroupObject<E>,
        chatMessages: List<ChatMessageObject<V>>,
    )

    suspend fun getTransactionByChatMessageId(chatMessageId: Long): List<W>

    fun allChatGroup(): Flow<List<T>>

    suspend fun allChatMessage(chatGroupId: Long): List<U>

    suspend fun deleteChatGroupAndMessage(chatGroupId: Long)
}