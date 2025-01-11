package com.harissabil.damome.di

import com.harissabil.damome.MainViewModel
import com.harissabil.damome.ui.screen.damommy.DamommyViewModel
import com.harissabil.damome.ui.screen.damommy_chat.DamommyChatViewModel
import com.harissabil.damome.ui.screen.home.HomeViewModel
import com.harissabil.damome.ui.screen.onboarding.OnboardingViewModel
import com.harissabil.damome.ui.screen.records.RecordsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::RecordsViewModel)
    viewModelOf(::DamommyViewModel)
    viewModelOf(::DamommyChatViewModel)
}