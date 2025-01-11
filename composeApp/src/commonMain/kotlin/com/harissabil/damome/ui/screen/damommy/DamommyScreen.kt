package com.harissabil.damome.ui.screen.damommy

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.ui.components.BaseTopAppBar
import com.harissabil.damome.ui.components.CustomSnackbarHost
import com.harissabil.damome.ui.screen.damommy.components.ChatHistoryItem
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.img_damommy
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalSharedTransitionApi::class, KoinExperimentalAPI::class)
@Composable
fun SharedTransitionScope.DamommyScreen(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onFabClick: () -> Unit,
    onHistoryClick: (chatGroupId: Long) -> Unit,
) {
    val viewModel: DamommyViewModel = koinViewModel()
    val chatHistories by viewModel.chatHistories.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        topBar = { BaseTopAppBar(title = "DaMommy") },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(MaterialTheme.spacing.small).sharedBounds(
                    sharedContentState = rememberSharedContentState("DamommyChatScreen"),
                    animatedVisibilityScope = animatedVisibilityScope
                ),
                containerColor = MiuixTheme.colorScheme.primary,
                shape = FloatingActionButtonDefaults.shape,
                onClick = onFabClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "Chat",
                    tint = Color.White
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = MaterialTheme.spacing.small)
        ) {
            if (chatHistories.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                        .padding(bottom = MaterialTheme.spacing.small)
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .padding(MaterialTheme.spacing.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(120.dp),
                        painter = painterResource(Res.drawable.img_damommy),
                        contentDescription = "DaMommy",
                    )
                    Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                    Text(
                        text = "DaMommy",
                        style = MiuixTheme.textStyles.title3.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = aiDescription(),
                        style = MiuixTheme.textStyles.subtitle,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
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
                    item {
                        Text(
                            modifier = Modifier.alpha(0.5f).animateItem(),
                            text = "Chat History",
                            style = MiuixTheme.textStyles.subtitle
                        )
                    }
                    items(
                        items = chatHistories.sortedByDescending { it.timestamp },
                        key = { it.id!! })
                    { chatHistoryItem ->
                        ChatHistoryItem(
                            modifier = Modifier.animateItem(),
                            chatGroup = chatHistoryItem,
                            onClick = onHistoryClick,
                            onDeleteClick = viewModel::deleteChatGroup
                        )
                        if (chatHistoryItem == chatHistories.minByOrNull { it.timestamp }!!) {
                            Spacer(modifier = Modifier.height(81.dp))
                        }
                    }
                }
            }
        }
    }
}

// the RAG is only for android currently, desktop soon after fixing the objectbox issue on desktop
expect fun aiDescription(): String