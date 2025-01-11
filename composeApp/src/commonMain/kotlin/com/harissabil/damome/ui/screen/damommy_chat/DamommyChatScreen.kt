package com.harissabil.damome.ui.screen.damommy_chat

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.ui.screen.damommy_chat.components.ChatItem
import com.harissabil.damome.ui.screen.damommy_chat.components.ChatSettingsBottomSheet
import com.harissabil.damome.ui.screen.damommy_chat.components.ChatTextField
import com.harissabil.damome.ui.screen.damommy_chat.components.DamommyTopAppBar
import com.harissabil.damome.ui.screen.damommy_chat.components.ScrollToBottomButton
import kotlinx.coroutines.launch
import network.chaintech.kmp_date_time_picker.ui.date_range_picker.WheelDateRangePickerDialog
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(
    ExperimentalSharedTransitionApi::class, KoinExperimentalAPI::class,
    ExperimentalMaterial3Api::class, ExperimentalUuidApi::class
)
@Composable
fun SharedTransitionScope.DamommyChatScreen(
    modifier: Modifier = Modifier,
    chatGroupId: Long? = null,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateUp: () -> Unit,
) {
    val viewModel: DamommyChatViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    // show scroll to bottom button when the list is scrolled up
    val showButton: Boolean by remember(lazyListState) {
        derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index != lazyListState.layoutInfo.totalItemsCount - 1 }
    }

    val settingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSettingsVisible by rememberSaveable { mutableStateOf(false) }

    var isDateRangePickerExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = chatGroupId) {
        if (chatGroupId != null) {
            viewModel.initChat(chatGroupId)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().sharedBounds(
            sharedContentState = rememberSharedContentState(if (chatGroupId == null) "DamommyChatScreen" else ""),
            animatedVisibilityScope = animatedVisibilityScope
        ),
        topBar = { DamommyTopAppBar(onNavigateUp = onNavigateUp) },
        containerColor = MiuixTheme.colorScheme.background,
        contentColor = MiuixTheme.colorScheme.onBackground,
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.chatMessages.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = MaterialTheme.spacing.large),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Hello, dear! How can I help you make the most of your money today?",
                            style = MiuixTheme.textStyles.title3.copy(fontWeight = FontWeight.SemiBold),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            state = lazyListState,
                            contentPadding = PaddingValues(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            ),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall)
                        ) {
                            items(
                                items = state.chatMessages.sortedBy { it.order },
                                key = { Uuid.random() })
                            { chat ->
                                ChatItem(
                                    modifier = Modifier.animateItem(),
                                    chatMessage = chat
                                )
                            }
                        }

                        ScrollToBottomButton(
                            isVisible = showButton,
                            onClick = {
                                scope.launch {
                                    lazyListState.animateScrollToItem(state.chatMessages.size - 1)
                                }
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(
                    vertical = MaterialTheme.spacing.small,
                    horizontal = MaterialTheme.spacing.medium,
                ),
                verticalAlignment = Alignment.Bottom
            ) {
                if (isSettingsVisible()) {
                    Column(
                        modifier = Modifier.height(49.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { isSettingsVisible = true },
                            backgroundColor = MiuixTheme.colorScheme.surface
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                }
                ChatTextField(
                    modifier = Modifier.weight(1f),
                    label = if (query.isEmpty()) if (state.chatMessages.isEmpty()) "Chat with DaMommy" else "Reply to DaMommy" else "",
                    value = query,
                    onValueChange = viewModel::onQueryChanged,
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Column(
                    modifier = Modifier.height(49.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = viewModel::sendQuery,
                        backgroundColor = when {
                            state.isWaitingResponse -> MiuixTheme.colorScheme.primary
                            state.isWaitingResponse && query.isEmpty() -> MiuixTheme.colorScheme.primary
                            query.isEmpty() -> MiuixTheme.colorScheme.disabledPrimary
                            else -> MiuixTheme.colorScheme.primary
                        },
                        enabled = query.isNotBlank() && !state.isWaitingResponse
                    ) {
                        if (state.isWaitingResponse) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 3.dp,
                                color = MiuixTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }

        if (isSettingsVisible) {
            ChatSettingsBottomSheet(
                onDismissRequest = { isSettingsVisible = false },
                sheetState = settingsSheetState,
                selectedContextSize = state.selectedContextSize,
                onContextSizeSelected = viewModel::onSelectedContextSizeChanged,
                selectedFilterByTime = state.selectedFilterByTime,
                onFilterByTimeSelected = viewModel::onSelectedFilterByTimeChanged,
                fromDate = state.pickFromDate,
                toDate = state.pickToDate,
                isDatePickerExpanded = isDateRangePickerExpanded,
                onDatePickerExpandedChange = { isDateRangePickerExpanded = it },
            )
        }

        WheelDateRangePickerDialog(
            height = 200.dp,
            onDismiss = { isDateRangePickerExpanded = false },
            initialFromDate = state.pickFromDate,
            initialToDate = state.pickToDate,
            selectFutureDate = false,
            containerColor = MiuixTheme.colorScheme.surface,
            dateTextColor = MiuixTheme.colorScheme.onSurface,
            dateTextStyle = MiuixTheme.textStyles.title4,
            showDatePicker = isDateRangePickerExpanded,
            hideHeader = true,
            showMonthAsNumber = true,
            onFromDateChangeListener = viewModel::onPickFromDateChanged,
            onToDateChangeListener = viewModel::onPickToDateChanged,
        )
    }
}

expect fun isSettingsVisible(): Boolean