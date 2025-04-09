package com.harissabil.damome.ui.screen.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.core.utils.getSymbolsList
import com.harissabil.damome.ui.components.CustomSnackbarHost
import com.harissabil.damome.ui.components.LargeDropdownMenu
import com.harissabil.damome.ui.screen.onboarding.components.OnboardingCard
import com.harissabil.fisch.feature.onboarding.presentation.component.OnboardingImage
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.damome_promo
import damome.composeapp.generated.resources.damome_promo_dark
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(KoinExperimentalAPI::class)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit,
) {
    val viewModel: OnboardingViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val currencies = Currency.entries.getSymbolsList()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.messageFlow.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(key1 = state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToHome()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { CustomSnackbarHost(snackbarHostState) }
    ) {
        Box(modifier = modifier.fillMaxSize()) {

            OnboardingImage(
                image = if (isSystemInDarkTheme()) Res.drawable.damome_promo_dark else Res.drawable.damome_promo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnboardingCard(
                    title = "Welcome to DaMoMe",
                    description = "DaMoMe is a simple and easy-to-use income/expense tracker app powered by Kotlin Multiplatform and AI technology.",
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MiuixTheme.colorScheme.surface)
                        .padding(horizontal = MaterialTheme.spacing.large)
                        .animateContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    LargeDropdownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        items = currencies,
                        label = "Currency",
                        selectedIndex = state.selectedCurrencyIndex,
                        onItemSelected = { index, item ->
                            viewModel.onCurrencySelected(index, item)
                        }
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    Text(
                        text = "*More currencies will be added soon.",
                        style = MiuixTheme.textStyles.body2
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MiuixTheme.colorScheme.surface)
                        .padding(horizontal = MaterialTheme.spacing.large)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColorsPrimary(),

                        onClick = viewModel::setCurrency
                    ) {
                        Text(text = "Get Started", color = Color.White)
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(MaterialTheme.spacing.large)
                        .fillMaxWidth()
                        .background(MiuixTheme.colorScheme.surface)
                )
            }
        }
    }
}