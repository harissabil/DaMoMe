package com.harissabil.damome.di

import com.harissabil.damome.domain.use_case.ExtractTransactionDataUseCase
import com.harissabil.damome.domain.use_case.ExtractTransactionDataUseCaseImpl
import com.harissabil.damome.domain.use_case.SummarizeChatUseCase
import com.harissabil.damome.domain.use_case.SummarizeChatUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<ExtractTransactionDataUseCase> { ExtractTransactionDataUseCaseImpl(get()) }
    single<SummarizeChatUseCase> { SummarizeChatUseCaseImpl(get()) }
}