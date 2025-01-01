package com.harissabil.damome.di

import com.harissabil.damome.domain.use_case.ExtractTransactionDataUseCase
import com.harissabil.damome.domain.use_case.ExtractTransactionDataUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<ExtractTransactionDataUseCase> { ExtractTransactionDataUseCaseImpl(get()) }
}