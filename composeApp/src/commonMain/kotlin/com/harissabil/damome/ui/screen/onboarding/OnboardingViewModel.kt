package com.harissabil.damome.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.damome.core.utils.Result
import com.harissabil.damome.domain.repository.CurrencyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val currencyRepository: CurrencyRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

    fun onCurrencySelected(index: Int, currency: String) {
        _state.update { it.copy(selectedCurrencyIndex = index, selectedCurrency = currency) }
    }

    fun setCurrency() = viewModelScope.launch {
        when (val result = currencyRepository.setCurrency(state.value.selectedCurrency)) {
            is Result.Error -> {
                _messageFlow.emit(result.message)
            }

            is Result.Success -> {
                _state.update { it.copy(isSuccess = true) }
            }
        }
    }
}