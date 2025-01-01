package com.harissabil.damome.ui.screen.onboarding

data class OnboardingState(
    val selectedCurrency: String = "IDR",
    val selectedCurrencyIndex: Int = 0,
    val isSuccess: Boolean = false,
)