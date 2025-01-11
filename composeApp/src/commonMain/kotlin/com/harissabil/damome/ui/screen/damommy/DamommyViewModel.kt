package com.harissabil.damome.ui.screen.damommy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DamommyViewModel(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _chatHistories = MutableStateFlow(emptyList<ChatGroup>())
    val chatHistories: StateFlow<List<ChatGroup>> = _chatHistories

    init {
        getChatHistory()
    }

    private fun getChatHistory() = viewModelScope.launch {
        chatRepository.getChatHistory().collect { histories ->
            _chatHistories.update { histories }
        }
    }

    fun deleteChatGroup(chatGroupId: Long) = viewModelScope.launch {
        chatRepository.deleteChatGroup(chatGroupId)
    }
}