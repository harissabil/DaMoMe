package com.harissabil.damome.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.harissabil.damome.data.local.entity.ChatGroupEntity
import com.harissabil.damome.data.local.entity.ChatGroupWithChatMessages
import com.harissabil.damome.data.local.entity.ChatMessageEntity
import com.harissabil.damome.data.local.entity.ChatMessageTransactionCrossRef
import com.harissabil.damome.data.local.entity.ChatMessageWithTransactions
import com.harissabil.damome.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    // Insert or update a chat group
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putChatGroup(chatGroup: ChatGroupEntity): Long

    // Insert or update a chat message
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putChatMessage(chatMessage: ChatMessageEntity): Long

    // Insert or update a chat message and its transactions
    @Transaction
    suspend fun putChatMessageAndTransactionToChatGroup(
        chatGroupId: Long,
        chatMessage: ChatMessageEntity,
        transactions: List<TransactionEntity>,
    ) {
        val messageId = putChatMessage(chatMessage)
        transactions.forEach { transaction ->
            insertChatMessageTransactionCrossRef(
                ChatMessageTransactionCrossRef(
                    chatMessageId = messageId,
                    transactionId = putTransaction(transaction)
                )
            )
        }
    }

    // Insert or update transactions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putTransaction(transaction: TransactionEntity): Long

    // Insert the relationship between a chat message and a transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessageTransactionCrossRef(crossRef: ChatMessageTransactionCrossRef)

    // Get all chat groups as a Flow
    @Query("SELECT * FROM ChatGroupEntity")
    fun allChatGroup(): Flow<List<ChatGroupEntity>>

    @Transaction
    @Query("SELECT * FROM ChatGroupEntity")
    fun allChatGroupWithMessages(): Flow<List<ChatGroupWithChatMessages>>

    // Get all chat messages for a specific chat group
    @Transaction
    @Query("SELECT * FROM ChatMessageEntity WHERE chatGroupId = :chatGroupId ORDER BY `order` ASC")
//    suspend fun allChatMessage(chatGroupId: Long): List<ChatMessageWithTransactions>
    suspend fun allChatMessage(chatGroupId: Long): List<ChatMessageEntity>

    // Delete a chat group and its messages
    @Query("DELETE FROM ChatGroupEntity WHERE id = :chatGroupId")
    suspend fun deleteChatGroup(chatGroupId: Long)

    @Query("DELETE FROM ChatMessageEntity WHERE chatGroupId = :chatGroupId")
    suspend fun deleteChatMessages(chatGroupId: Long)

    @Transaction
    suspend fun deleteChatGroupAndMessage(chatGroupId: Long) {
        deleteChatMessages(chatGroupId)
        deleteChatGroup(chatGroupId)
    }

    // Get transactions by chat message ID
    @Transaction
    @Query("SELECT * FROM TransactionEntity WHERE id IN (SELECT transactionId FROM ChatMessageTransactionCrossRef WHERE chatMessageId = :chatMessageId)")
    suspend fun getTransactionByChatMessageId(chatMessageId: Long): List<TransactionEntity>
}