package com.harissabil.damome.ui.screen.damommy_chat

import com.harissabil.damome.domain.model.ChatMessage
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.LocalDate

data class DamommyChatState(
    val chatGroupId: Long? = null,

    val isWaitingResponse: Boolean = false,
    val chatMessages: List<ChatMessage> = emptyList(),

    val selectedContextSize: ContextSize = ContextSize.MEDIUM,
    val selectedFilterByTime: FilterByTime = FilterByTime.ALL,

    val pickFromDate: LocalDate = LocalDate.now().minusDays(7),
    val pickToDate: LocalDate = LocalDate.now(),
)
