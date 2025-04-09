package com.harissabil.damome.ui.screen.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.core.utils.formatToTextToEmbed
import com.harissabil.damome.core.utils.parseFormattedAmount
import com.harissabil.damome.core.utils.toCurrency
import com.harissabil.damome.domain.model.Category
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.TransactionType
import com.harissabil.damome.domain.repository.CurrencyRepository
import com.harissabil.damome.domain.repository.TextEmbeddingRepository
import com.harissabil.damome.domain.repository.TransactionRepository
import com.harissabil.damome.ui.screen.home.TransactionToSubmitState
import com.harissabil.damome.ui.screen.home.isTextEmbeddingNeeded
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class RecordsViewModel(
    private val transactionRepository: TransactionRepository,
    private val currencyRepository: CurrencyRepository,
    private val textEmbeddingRepository: TextEmbeddingRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _transactions = MutableStateFlow(emptyList<Transaction>())

    val filteredTransactions = combine(_transactions, searchQuery) { transactions, query ->
        transactions.filter {
            it.category.contains(query, ignoreCase = true) ||
                    it.description?.contains(query, ignoreCase = true) ?: false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val _currency = MutableStateFlow(Currency.EMPTY)
    val currency: StateFlow<Currency> = _currency.asStateFlow()

    private val _transactionToSubmitState = MutableStateFlow(TransactionToSubmitState())
    val transactionToSubmitState: StateFlow<TransactionToSubmitState> =
        _transactionToSubmitState.asStateFlow()

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

    init {
        getTransactions()
        getCurrency()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.update { query }
    }

    private fun getTransactions() = viewModelScope.launch {
        transactionRepository.getAllTransactions().collect { transactions ->
            _transactions.update { transactions }
        }
    }

    private fun getCurrency() = viewModelScope.launch {
        currencyRepository.getCurrencyFlow().collect { currency ->
            _currency.update { currency.first().toCurrency() }
        }
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.deleteTransaction(transaction)
        _messageFlow.emit("Transaction deleted!")
    }

    // Below is for the transaction to submit state methods
    fun onAmoutChanged(amount: String) {
        val parsedAmount = parseFormattedAmount(amount, _currency.value)
        _transactionToSubmitState.update { it.copy(amount = parsedAmount) }
    }

    fun onDateAndTimeChanged(dateTime: LocalDateTime) {
        _transactionToSubmitState.update { it.copy(timestamp = dateTime.toInstant(TimeZone.currentSystemDefault())) }
    }

    fun onCategoryChanged(category: String) {
        _transactionToSubmitState.update { it.copy(category = category) }
    }

    fun onDescriptionChanged(description: String) {
        _transactionToSubmitState.update { it.copy(description = description) }
    }

    fun onTransactionTypeChanged(transactionType: TransactionType) {
        _transactionToSubmitState.update { it.copy(type = transactionType) }
    }

    fun editTransaction(transaction: Transaction) {
        _transactionToSubmitState.update {
            it.copy(
                transactionToEdit = transaction,
                id = transaction.id,
                type = transaction.type,
                timestamp = transaction.timestamp,
                currency = transaction.currency,
                amount = transaction.amount,
                category = transaction.category,
                description = transaction.description,
                textToEmbed = transaction.textToEmbed
            )
        }
    }

    fun saveEditedTransaction() = viewModelScope.launch {
        _transactionToSubmitState.update { it.copy(isLoading = true) }

        val description = _transactionToSubmitState.value.description
        val textToEmbed = _transactionToSubmitState.value.textToEmbed

        val textToEmbedAgain = description?.let {
            formatToTextToEmbed(
                transactionType = _transactionToSubmitState.value.type,
                amount = _transactionToSubmitState.value.amount!!,
                category = Category.entries.find { it.value == _transactionToSubmitState.value.category }!!,
                currency = _currency.value,
                description = it
            )
        }

        val embedding = if (textToEmbed != textToEmbedAgain) {
            if (isTextEmbeddingNeeded()) {
                if (description != null && textToEmbedAgain != null) {
                    println("Text to embed again: $textToEmbedAgain")
                    when (val result = textEmbeddingRepository.getEmbedding(textToEmbedAgain)) {
                        is Result.Error -> {
                            _messageFlow.emit(result.message)
                            _transactionToSubmitState.update { it.copy(isLoading = false) }
                            return@launch
                        }

                        is Result.Success -> result.data.values
                    }
                } else _transactionToSubmitState.value.transactionToEdit?.embedding
            } else _transactionToSubmitState.value.transactionToEdit?.embedding
        } else _transactionToSubmitState.value.transactionToEdit?.embedding

        val transactionToUpdate = Transaction(
            id = _transactionToSubmitState.value.transactionToEdit?.id,
            type = _transactionToSubmitState.value.type,
            timestamp = _transactionToSubmitState.value.timestamp,
            currency = _transactionToSubmitState.value.currency ?: _currency.value,
            amount = _transactionToSubmitState.value.amount ?: 0.0,
            category = _transactionToSubmitState.value.category ?: "bills",
            description = _transactionToSubmitState.value.description,
            textToEmbed = textToEmbedAgain,
            embedding = embedding
        )

        when (val result = transactionRepository.updateTransaction(transactionToUpdate)) {
            is Result.Error -> {
                _messageFlow.emit(result.message)
                _transactionToSubmitState.update {
                    it.copy(
                        isFailed = true,
                        isLoading = false
                    )
                }
            }

            is Result.Success -> {
                _messageFlow.emit("Transaction updated!")
                _transactionToSubmitState.update {
                    it.copy(
                        isLoading = false,
                        isSavedSuccess = true
                    )
                }
                resetTransactionToSubmitState()
            }
        }
    }

    fun resetTransactionToSubmitState() {
        _transactionToSubmitState.update { TransactionToSubmitState() }
    }
}