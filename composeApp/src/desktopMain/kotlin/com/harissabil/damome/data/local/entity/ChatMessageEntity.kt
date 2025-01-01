package com.harissabil.damome.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    foreignKeys = [ForeignKey(
        entity = ChatGroupEntity::class,
        parentColumns = ["id"],
        childColumns = ["chatGroupId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["chatGroupId"])] // Add an index for chatGroupId
)
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = null,
    override var chatGroupId: Long,
    override var isUser: Boolean = false,
    override var order: Int = 0,
    override var message: String = "",
    override var timestamp: Instant = Clock.System.now(),
) : IChatMessageEntity
