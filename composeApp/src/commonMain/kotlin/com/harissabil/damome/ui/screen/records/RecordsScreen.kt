package com.harissabil.damome.ui.screen.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.harissabil.damome.core.utils.toMonthAndYear
import com.harissabil.damome.domain.model.Category
import com.harissabil.damome.ui.components.AddTransactionBottomSheet
import com.harissabil.damome.ui.components.BaseTopAppBar
import com.harissabil.damome.ui.components.CustomSnackbarHost
import com.harissabil.damome.ui.screen.records.components.RecordItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerDialog
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(modifier: Modifier = Modifier) {
    val viewModel: RecordsViewModel = koinViewModel()

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val transactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()

    val groupedTransactionsByMonthAndYear = remember(transactions) {
        transactions
            .sortedByDescending { it.timestamp }
            .groupBy { it.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).date.toMonthAndYear() }
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
        topBar = { BaseTopAppBar(title = "Records") }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = MaterialTheme.spacing.small)
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                inputField = {
                    TextField(
                        leadingIcon = {
                            Row {
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                )
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                            }
                        },
                        label = if (searchQuery.isBlank()) "Search transactions" else "",
                        value = searchQuery,
                        onValueChange = viewModel::onSearchQueryChanged,
                    )
                },
                expanded = false,
                onExpandedChange = {}
            ) {}

            if (transactions.isEmpty()) {
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
            } else {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(
                        top = 0.dp,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    groupedTransactionsByMonthAndYear.forEach { (monthAndYear, transactions) ->
                        item {
                            Text(
                                modifier = Modifier.alpha(0.5f).animateItem(),
                                text = monthAndYear,
                                style = MiuixTheme.textStyles.subtitle
                            )
                        }
                        items(items = transactions, key = { it.id!! }) { transaction ->
                            RecordItem(
                                modifier = Modifier.animateItem(),
                                transaction = transaction,
                                onEditClick = {
                                    isAddTransactionBottomSheetVisible = true
                                    viewModel.editTransaction(transaction)
                                },
                                onDeleteClick = viewModel::deleteTransaction
                            )
                        }
                    }
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
                currency = transactionToSubmitState.currency
                    ?: viewModel.currency.collectAsState().value,
                amount = transactionToSubmitState.amount ?: 0.0,
                scannedAmount = null,
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