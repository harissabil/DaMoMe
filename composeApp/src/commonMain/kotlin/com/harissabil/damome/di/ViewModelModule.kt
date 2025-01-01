package com.harissabil.damome.di

import com.harissabil.damome.MainViewModel
import com.harissabil.damome.ui.screen.home.HomeViewModel
import com.harissabil.damome.ui.screen.onboarding.OnboardingViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
}