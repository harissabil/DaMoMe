package com.harissabil.damome.di

import com.harissabil.damome.data.local.repository.CurrencyRepositoryImpl
import com.harissabil.damome.data.local.repository.TransactionRepositoryImpl
import com.harissabil.damome.data.remote.text_embedding.repository.TextEmbeddingRepositoryImpl
import com.harissabil.damome.domain.repository.CurrencyRepository
import com.harissabil.damome.domain.repository.TextEmbeddingRepository
import com.harissabil.damome.domain.repository.TransactionRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule = module {
    single<TextEmbeddingRepository> { TextEmbeddingRepositoryImpl(get()) }
}

expect val sharedRepositoryModule: Module