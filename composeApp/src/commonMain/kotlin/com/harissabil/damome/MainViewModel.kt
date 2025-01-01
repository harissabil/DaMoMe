package com.harissabil.damome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.domain.repository.CurrencyRepository
import com.harissabil.damome.ui.navigation.Route
import kotlinx.coroutines.launch

class MainViewModel(
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {

    var startRoute by mutableStateOf<Route>(Route.Home)
        private set

    init {
        getAppEntry()
    }

    private fun getAppEntry() = viewModelScope.launch {
        startRoute = when (val currencyResult = currencyRepository.getCurrency()) {
            is Result.Error -> {
                Route.Onboarding
            }

            is Result.Success -> {
                if (currencyResult.data != null) {
                    Route.Home
                } else {
                    Route.Onboarding
                }
            }
        }
    }
}