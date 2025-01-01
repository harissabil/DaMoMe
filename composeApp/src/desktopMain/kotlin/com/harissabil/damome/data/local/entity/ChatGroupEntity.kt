package com.harissabil.damome.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity
data class ChatGroupEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = null,
    override var name: String,
    override var timestamp: Instant
): IChatGroupEntity
