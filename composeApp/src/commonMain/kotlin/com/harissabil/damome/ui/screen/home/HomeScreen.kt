package com.harissabil.damome.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.domain.model.Category
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.ui.components.AddTransactionBottomSheet
import com.harissabil.damome.ui.components.CustomSnackbarHost
import com.harissabil.damome.ui.screen.home.components.HomeTopAppBar
import com.harissabil.damome.ui.screen.home.components.IncomeExpenseWithFilter
import com.harissabil.damome.ui.screen.home.components.TotalBalanceCard
import com.harissabil.damome.ui.screen.home.components.TransactionDayItem
import com.harissabil.damome.ui.screen.home.components.WeekCalendarHeader
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerDialog
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val perDateTransactions by viewModel.perDateTransactions.collectAsStateWithLifecycle()

    val perDateTransactionsSorted by remember(key1 = perDateTransactions) {
        mutableStateOf(perDateTransactions.sortedBy { it.timestamp })
    }

    val transactionToSubmitState by viewModel.transactionToSubmitState.collectAsStateWithLifecycle()

    val addTransactionBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isAddTransactionBottomSheetVisible by rememberSaveable { mutableStateOf(false) }

    var isDateTimePickerExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.messageFlow.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        topBar = {
            HomeTopAppBar()
        },
        floatingActionButton = {
            // Workaround for snackbars not showing above FAB
            FloatingActionButton(
                modifier = Modifier.padding(MaterialTheme.spacing.small).alpha(0f),
                onClick = {}) {}
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(vertical = MaterialTheme.spacing.small),
        ) {
            TotalBalanceCard(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                currency = state.currency,
                value = state.totalBalance
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
            IncomeExpenseWithFilter(
                currency = state.currency,
                totalIncome = state.totalIncome,
                totalIncomeDaily = state.totalIncomeDaily,
                totalIncomeWeekly = state.totalIncomeWeekly,
                totalIncomeMonthly = state.totalIncomeMonthly,
                totalIncomeYearly = state.totalIncomeYearly,
                totalExpense = state.totalExpense,
                totalExpenseDaily = state.totalExpenseDaily,
                totalExpenseWeekly = state.totalExpenseWeekly,
                totalExpenseMonthly = state.totalExpenseMonthly,
                totalExpenseYearly = state.totalExpenseYearly,
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall))
            WeekCalendarHeader(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                currentDate = state.currentDate,
                startDate = remember { LocalDate.now().minusDays(500) },
                endDate = remember { LocalDate.now().plusDays(500) },
                selection = selectedDate,
                onDateClick = viewModel::onDateSelected
            )
            if (perDateTransactionsSorted.isNotEmpty()) {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                perDateTransactionsSorted.forEach { transaction: Transaction ->
                    TransactionDayItem(
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                        transaction = transaction,
                        onEditClick = {
                            isAddTransactionBottomSheetVisible = true
                            viewModel.editTransaction(transaction)
                        },
                        onDeleteClick = viewModel::deleteTransaction
                    )
                    if (perDateTransactionsSorted.last() == transaction) {
                        Spacer(modifier = Modifier.height(81.dp))
                    } else {
                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(60.dp).alpha(0.5f),
                        imageVector = Icons.Outlined.Savings,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(MaterialTheme.spacing.medium))
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = "No income/expense yet...",
                        textAlign = TextAlign.Center,
                        style = MiuixTheme.textStyles.title4
                    )
                }
            }
        }

        if (isAddTransactionBottomSheetVisible) {
            AddTransactionBottomSheet(
                onDismissRequest = {
                    scope.launch { addTransactionBottomSheetState.hide() }.invokeOnCompletion {
                        if (!addTransactionBottomSheetState.isVisible) {
                            isAddTransactionBottomSheetVisible = false
                            viewModel.resetTransactionToSubmitState()
                        }
                    }
                },
                sheetState = addTransactionBottomSheetState,
                currency = transactionToSubmitState.currency ?: state.currency,
                amount = transactionToSubmitState.amount ?: 0.0,
                scannedAmount = transactionToSubmitState.scannedAmount,
                onAmountChange = viewModel::onAmoutChanged,
                dateAndTime = transactionToSubmitState.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()),
                onDateAndTimeChange = viewModel::onDateAndTimeChanged,
                isDateTimePickerExpanded = isDateTimePickerExpanded,
                onDateTimePickerExpandedChange = { isDateTimePickerExpanded = it },
                category = transactionToSubmitState.category ?: Category.BILLS.value,
                onCategoryChange = viewModel::onCategoryChanged,
                description = transactionToSubmitState.description,
                onDescriptionChange = viewModel::onDescriptionChanged,
                transactionType = transactionToSubmitState.type,
                onTransactionTypeChange = viewModel::onTransactionTypeChanged,
                isLoading = transactionToSubmitState.isLoading,
                submitText = if (transactionToSubmitState.transactionToEdit != null) "Update" else "Save",
                onSubmitTransaction = {
                    viewModel.saveEditedTransaction()
                    scope.launch { addTransactionBottomSheetState.hide() }.invokeOnCompletion {
                        if (!addTransactionBottomSheetState.isVisible) {
                            isAddTransactionBottomSheetVisible = false
                        }
                    }
                },
            )
        }

        WheelDateTimePickerDialog(
            height = 200.dp,
            onDismiss = { isDateTimePickerExpanded = false },
            startDate = transactionToSubmitState.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()),
            containerColor = MiuixTheme.colorScheme.surface,
            dateTextColor = MiuixTheme.colorScheme.onSurface,
            dateTextStyle = MiuixTheme.textStyles.title4,
            showDatePicker = isDateTimePickerExpanded,
            hideHeader = true,
            showMonthAsNumber = true,
            onDateChangeListener = viewModel::onDateAndTimeChanged,
        )
    }
}