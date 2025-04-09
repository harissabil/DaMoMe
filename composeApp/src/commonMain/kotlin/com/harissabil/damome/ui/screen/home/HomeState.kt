package com.harissabil.damome.ui.screen.home

import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.TransactionType
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class HomeState(
    val currency: Currency = Currency.EMPTY  ,
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalIncomeDaily: Double = 0.0,
    val totalIncomeWeekly: Double = 0.0,
    val totalIncomeMonthly: Double = 0.0,
    val totalIncomeYearly: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalExpenseDaily: Double = 0.0,
    val totalExpenseWeekly: Double = 0.0,
    val totalExpenseMonthly: Double = 0.0,
    val totalExpenseYearly: Double = 0.0,

    val currentDate: LocalDate = LocalDate.now(),
    val perDateTransaction: List<Transaction> = emptyList(),
)

data class TransactionToSubmitState(
    val transactionToEdit: Transaction? = null,
    val id: Long? = null,
    val type: TransactionType = TransactionType.INCOME,
    val timestamp: Instant = Clock.System.now(),
    val amount: Double? = 0.0,
    val currency: Currency? = null ,
    val category: String? = null,
    val description: String? = null,
    val textToEmbed: String? = null,
    val isLoading: Boolean = false,
    val isFailed: Boolean = false,
    val isSavedSuccess: Boolean = false,

    val scannedAmount: Double? = null,
)
