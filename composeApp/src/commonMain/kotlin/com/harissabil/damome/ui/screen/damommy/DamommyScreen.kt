package com.harissabil.damome.ui.screen.damommy

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.ui.components.CustomSnackbarHost
import com.harissabil.damome.ui.screen.damommy.components.DamommyTopAppBar
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import top.yukonga.miuix.kmp.basic.Scaffold

@OptIn(ExperimentalSharedTransitionApi::class, KoinExperimentalAPI::class)
@Composable
fun SharedTransitionScope.DamommyScreen(
    modifier: Modifier = Modifier,
    chatGroupId: Long? = null,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateUp: () -> Unit,
) {
    val viewModel: DamommyViewModel = koinViewModel()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize().sharedBounds(
            sharedContentState = rememberSharedContentState("DamommyScreen"),
            animatedVisibilityScope = animatedVisibilityScope
        ),
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        topBar = { DamommyTopAppBar(onNavigateUp = onNavigateUp) },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(vertical = MaterialTheme.spacing.small)
                .padding(horizontal = MaterialTheme.spacing.medium),
        ) {

        }
    }
}