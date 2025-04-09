package com.harissabil.damome.ui.screen.damommy_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.data.remote.gemini.GeminiClient
import com.harissabil.damome.domain.model.ChatGroup
import com.harissabil.damome.domain.model.ChatMessage
import com.harissabil.damome.domain.model.DamomeResponse
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.repository.ChatRepository
import com.harissabil.damome.domain.repository.TextEmbeddingRepository
import com.harissabil.damome.domain.repository.TransactionRepository
import com.harissabil.damome.domain.use_case.SummarizeChatUseCase
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import network.chaintech.kmp_date_time_picker.utils.MIN
import network.chaintech.kmp_date_time_picker.utils.withDayOfMonth

class DamommyChatViewModel(
    private val chatRepository: ChatRepository,
    private val transactionRepository: TransactionRepository,
    private val textEmbeddingRepository: TextEmbeddingRepository,
    private val geminiClient: GeminiClient,
    private val summarizeChatUseCase: SummarizeChatUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DamommyChatState())
    val state: StateFlow<DamommyChatState> = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // The rag only works on Android for now
    private val chatModel =
        if (isSettingsVisible()) geminiClient.damommyRagModel else geminiClient.damommyModel
    private var chat = chatModel.startChat()
    private var chatOrder = -1

    fun sendQuery() = viewModelScope.launch {
        val userMessage = ChatMessage(
            id = null,
            isUser = true,
            order = chatOrder++,
            message = _query.value,
            chatGroupId = _state.value.chatGroupId ?: 0L,
            relatedTransactions = null,
            timestamp = Clock.System.now()
        )
        _state.update {
            it.copy(
                isWaitingResponse = true,
                chatMessages = it.chatMessages.toMutableList().apply {
                    add(
                        index = chatOrder,
                        element = userMessage
                    )
                },
            )
        }
        _query.update { "" }

        if (chatOrder == 0) {
            saveFirstChatMessage(userMessage)
        } else {
            sendPrompt(userMessage)
        }
    }

    private fun saveFirstChatMessage(userMessage: ChatMessage) = viewModelScope.launch {
        when (val summaryResult = summarizeChatUseCase(userMessage.message)) {
            is Result.Error -> onErrorChatResponse()
            is Result.Success -> {
                val summary = summaryResult.data
                val chatGroup = ChatGroup(
                    id = null,
                    name = summary,
                    timestamp = Clock.System.now()
                )
                val chatGroupId = chatRepository.saveChatGroup(chatGroup)
                _state.update {
                    it.copy(
                        chatGroupId = chatGroupId,
                        chatMessages = it.chatMessages.map { chatMessage ->
                            chatMessage.copy(chatGroupId = chatGroupId)
                        }
                    )
                }
            }
        }

        try {
            val userMessageToSave = userMessage.copy(chatGroupId = _state.value.chatGroupId!!)
            sendPrompt(userMessageToSave)
        } catch (e: Exception) {
            println("Error on saveFirstChatMessage: $e")
            onErrorChatResponse()
        }
    }

    private fun sendPrompt(userMessage: ChatMessage) = viewModelScope.launch {
        val retrievedContext: List<Transaction> = if (isSettingsVisible()) {
            when (val queryEmbeddingResult =
                textEmbeddingRepository.getEmbedding(userMessage.message)) {
                is Result.Error -> emptyList()
                is Result.Success -> {
                    transactionRepository.retrieveSimilarTransactions(
                        queryEmbeddingResult.data.values,
                        _state.value.selectedContextSize.value,
                        fromDate = _state.value.pickFromDate,
                        toDate = _state.value.pickToDate
                    )
                }
            }
        } else emptyList()

        println("Retrieved Context: $retrievedContext")

        try {
            val response = chat.sendMessage(
                if (retrievedContext.isEmpty()) {
                    userMessage.message
                } else {
                    """
                        Here is the retrieved context
                        --------------------------------------------------
                        ${retrievedContext.map { it.toTransactionChatContext() }.joinToString("\n") { it.toString() }}
                        --------------------------------------------------
                        Here is the user\'s query: ${userMessage.message}
                    """.trimIndent()
                }
            )
            println("Response: ${response.text}")
            val cleanedResponse = geminiClient.extractResultFromResponse(response.text!!)
            println("Cleaned Response: $cleanedResponse")
            val data = Json.decodeFromString<DamomeResponse>(cleanedResponse)
            println("Data: $data")

            // Save user message
            chatRepository.saveChatMessage(
                chatGroupId = _state.value.chatGroupId!!,
                messages = userMessage
            )

            _state.update {
                it.copy(
                    isWaitingResponse = false,
                    chatMessages = it.chatMessages.toMutableList().apply {
                        val relatedTransactions = if (data.showRelatedTransaction) {
                            retrievedContext.filter { context -> context.id in data.relatedTransactionIds!! }
                        } else null

                        val responseMessage = ChatMessage(
                            id = null,
                            isUser = false,
                            order = chatOrder++,
                            message = data.message,
                            chatGroupId = _state.value.chatGroupId ?: 0L,
                            relatedTransactions = relatedTransactions,
                            timestamp = Clock.System.now()
                        )
                        // Save response message
                        chatRepository.saveChatMessage(
                            chatGroupId = _state.value.chatGroupId!!,
                            messages = responseMessage
                        )
                        add(
                            index = chatOrder,
                            element = responseMessage
                        )
                    }
                )
            }
        } catch (e: Exception) {
            println("Error on sendPrompt: $e")
            onErrorChatResponse()
        }
    }

    private fun onErrorChatResponse() {
        _state.update {
            it.copy(
                isWaitingResponse = false,
                chatMessages = it.chatMessages.toMutableList().apply {
                    add(
                        index = chatOrder++,
                        element = ChatMessage(
                            id = null,
                            isUser = false,
                            order = chatOrder,
                            message = "Sorry, I couldn't process your request. Please try again.",
                            chatGroupId = _state.value.chatGroupId ?: 0L,
                            relatedTransactions = null,
                            timestamp = Clock.System.now()
                        )
                    )
                }
            )
        }
    }

    fun initChat(chatGroupId: Long) = viewModelScope.launch {
        val chatMessages = chatRepository.getChatMessage(chatGroupId)
        chatOrder = chatMessages.size - 1
        _state.update {
            it.copy(
                chatGroupId = chatGroupId,
                chatMessages = chatMessages,
            )
        }
    }

    fun onQueryChanged(query: String) {
        _query.update { query }
    }

    fun onSelectedContextSizeChanged(selectedContextSize: ContextSize) {
        _state.update { it.copy(selectedContextSize = selectedContextSize) }
    }

    fun onSelectedFilterByTimeChanged(selectedFilterByTime: FilterByTime) {
        when (selectedFilterByTime) {
            FilterByTime.ALL -> _state.update {
                it.copy(
                    pickFromDate = LocalDate.MIN(),
                    pickToDate = LocalDate.now(),
                    selectedFilterByTime = selectedFilterByTime
                )
            }

            FilterByTime.LAST_7_DAYS -> _state.update {
                it.copy(
                    pickFromDate = LocalDate.now().minusDays(7),
                    pickToDate = LocalDate.now(),
                    selectedFilterByTime = selectedFilterByTime
                )
            }

            FilterByTime.THIS_MONTH -> _state.update {
                it.copy(
                    pickFromDate = LocalDate.now().withDayOfMonth(1),
                    pickToDate = LocalDate.now(),
                    selectedFilterByTime = selectedFilterByTime
                )
            }

            FilterByTime.PICK_DATE -> _state.update {
                it.copy(
                    selectedFilterByTime = selectedFilterByTime
                )
            }
        }
    }

    fun onPickFromDateChanged(pickFromDate: LocalDate) {
        _state.update { it.copy(pickFromDate = pickFromDate) }
    }

    fun onPickToDateChanged(pickToDate: LocalDate) {
        _state.update { it.copy(pickToDate = pickToDate) }
    }
}