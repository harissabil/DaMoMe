package com.harissabil.damome.ui.screen.home

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
import com.harissabil.damome.domain.use_case.ExtractTransactionDataUseCase
import com.kizitonwose.calendar.core.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class HomeViewModel(
    private val currencyRepository: CurrencyRepository,
    private val transactionRepository: TransactionRepository,
    private val textEmbeddingRepository: TextEmbeddingRepository,
    private val extractTransactionDataUseCase: ExtractTransactionDataUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _transactionToSubmitState = MutableStateFlow(TransactionToSubmitState())
    val transactionToSubmitState: StateFlow<TransactionToSubmitState> =
        _transactionToSubmitState.asStateFlow()

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val perDateTransactions = _selectedDate
        .flatMapLatest {
            transactionRepository.getPerDateTransactions(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    init {
        getCurrency()
        getTotalBalance()
        getIncomesAndExpenses()
    }

    private fun getCurrency() = viewModelScope.launch {
        currencyRepository.getCurrencyFlow().collect { currencies ->
            _state.update { it.copy(currency = currencies.firstOrNull()?.toCurrency() ?: Currency.EMPTY ) }
        }
    }

    private fun getTotalBalance() = viewModelScope.launch {
        transactionRepository.getTotalBalance().collect { totalBalance ->
            if (totalBalance < 0) {
                _state.update { it.copy(totalBalance = 0.0) }
            } else _state.update { it.copy(totalBalance = totalBalance) }
        }
    }

    private fun getIncomesAndExpenses() = viewModelScope.launch {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()
        val today = now.toLocalDateTime(tz).date
        val month = now.toLocalDateTime(tz).month
        val year = now.toLocalDateTime(tz).year

        val startInstantDate = today.atStartOfDayIn(tz)
        val endInstantDate = today.atTime(23, 59, 59).toInstant(tz)

        val startInstantWeek = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY)
            .atStartOfDayIn(tz)
        val endInstantWeek = startInstantWeek.plus(7.toDuration(DurationUnit.DAYS))
            .minus(1.toDuration(DurationUnit.MILLISECONDS))
            .toLocalDateTime(tz)
            .toInstant(tz)

        val startInstantMonth = LocalDate(year, month, 1).atStartOfDayIn(tz)
        val endInstantMonth =
            LocalDate(year, month, 1).plus(1, DateTimeUnit.MONTH).atStartOfDayIn(tz)
                .minus(duration = 1.toDuration(DurationUnit.MILLISECONDS))

        val startInstantYear = LocalDate(year, 1, 1).atStartOfDayIn(tz)
        val endInstantYear = LocalDate(year, 12, 31).atTime(23, 59, 59).toInstant(tz)

        launch {
            transactionRepository.getAllIncome().collect { incomes ->
                val allTime = incomes.sumOf { it.amount }
                val daily = incomes.filter { it.timestamp in startInstantDate..endInstantDate }
                    .sumOf { it.amount }
                val weekly = incomes.filter { it.timestamp in startInstantWeek..endInstantWeek }
                    .sumOf { it.amount }
                val monthly = incomes.filter { it.timestamp in startInstantMonth..endInstantMonth }
                    .sumOf { it.amount }
                val yearly = incomes.filter { it.timestamp in startInstantYear..endInstantYear }
                    .sumOf { it.amount }
                _state.update {
                    it.copy(
                        totalIncome = allTime,
                        totalIncomeDaily = daily,
                        totalIncomeWeekly = weekly,
                        totalIncomeMonthly = monthly,
                        totalIncomeYearly = yearly
                    )
                }
            }
        }

        launch {
            transactionRepository.getAllExpense().collect { expenses ->
                val allTime = expenses.sumOf { it.amount }
                val daily = expenses.filter { it.timestamp in startInstantDate..endInstantDate }
                    .sumOf { it.amount }
                val weekly = expenses.filter { it.timestamp in startInstantWeek..endInstantWeek }
                    .sumOf { it.amount }
                val monthly = expenses.filter { it.timestamp in startInstantMonth..endInstantMonth }
                    .sumOf { it.amount }
                val yearly = expenses.filter { it.timestamp in startInstantYear..endInstantYear }
                    .sumOf { it.amount }
                _state.update {
                    it.copy(
                        totalExpense = allTime,
                        totalExpenseDaily = daily,
                        totalExpenseWeekly = weekly,
                        totalExpenseMonthly = monthly,
                        totalExpenseYearly = yearly
                    )
                }
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.update { date }
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.deleteTransaction(transaction)
        _messageFlow.emit("Transaction deleted!")
    }

    // Below is for the transaction to submit state methods
    fun onAmoutChanged(amount: String) {
        val parsedAmount = parseFormattedAmount(amount, _state.value.currency)
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

    fun saveTransaction() = viewModelScope.launch {
        _transactionToSubmitState.update { it.copy(isLoading = true) }

        val description = _transactionToSubmitState.value.description
        val textToEmbed = description?.let {
            formatToTextToEmbed(
                transactionType = _transactionToSubmitState.value.type,
                amount = _transactionToSubmitState.value.amount!!,
                category = Category.entries.find { it.value == _transactionToSubmitState.value.category }
                    ?: Category.BILLS,
                currency = _state.value.currency,
                description = it
            )
        }

        val embedding = if (isTextEmbeddingNeeded()) {
            if (description != null && textToEmbed != null) {
                println("Text to embed: $textToEmbed")
                when (val result = textEmbeddingRepository.getEmbedding(textToEmbed)) {
                    is Result.Error -> {
                        _messageFlow.emit(result.message)
                        _transactionToSubmitState.update { it.copy(isLoading = false) }
                        return@launch
                    }

                    is Result.Success -> result.data.values
                }
            } else null
        } else null

        val transaction = Transaction(
            id = null,
            type = _transactionToSubmitState.value.type,
            timestamp = _transactionToSubmitState.value.timestamp,
            currency = _state.value.currency,
            amount = _transactionToSubmitState.value.amount ?: 0.0,
            category = _transactionToSubmitState.value.category ?: "bills",
            description = description,
            textToEmbed = textToEmbed,
            embedding = embedding
        )

        when (val result = transactionRepository.createTransaction(transaction)) {
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
                _messageFlow.emit("Transaction saved!")
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
                currency = _state.value.currency,
                description = it
            )
        }

        val embedding = if (textToEmbed != textToEmbedAgain) {
            if (isTextEmbeddingNeeded()) {
                if (description != null && textToEmbedAgain != null) {
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
            currency = _transactionToSubmitState.value.currency ?: _state.value.currency,
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

    fun onUploadedImageChanged(byteArray: ByteArray) = viewModelScope.launch {
        _transactionToSubmitState.update { it.copy(isLoading = true) }

        when (val result = extractTransactionDataUseCase(byteArray)) {
            is Result.Error -> {
                println("Error: ${result.message}")
                _messageFlow.emit(result.message)
                _transactionToSubmitState.update { it.copy(isLoading = false, isFailed = true) }
            }

            is Result.Success -> {
                println("Success: ${result.data}")
                _transactionToSubmitState.update {
                    it.copy(
                        amount = result.data.amount,
                        timestamp = result.data.dateTime?.let { it1 ->
                            LocalDateTime.parse(it1).toInstant(TimeZone.currentSystemDefault())
                        }
                            ?: Clock.System.now(),
                        category = result.data.category,
                        description = result.data.description,
                        type = TransactionType.entries.find { it.value == result.data.type }
                            ?: TransactionType.INCOME,
                        isLoading = false,

                        scannedAmount = result.data.amount,
                    )
                }
            }
        }
    }
}

// check if text embedding is needed or not
// desktop is not supported yet
expect fun isTextEmbeddingNeeded(): Boolean