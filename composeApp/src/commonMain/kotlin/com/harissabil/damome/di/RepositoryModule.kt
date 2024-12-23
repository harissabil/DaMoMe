package com.harissabil.damome.di

import com.harissabil.damome.data.remote.text_embedding.repository.TextEmbeddingRepositoryImpl
import com.harissabil.damome.domain.repository.TextEmbeddingRepository
import org.koin.dsl.module

val provideRepositoryModule = module {
    single<TextEmbeddingRepository> { TextEmbeddingRepositoryImpl(get()) }
}