package com.harissabil.damome.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            httpClientModule,
            databaseModule,
            repositoryModule,
            sharedRepositoryModule,
            viewModelModule,
            useCaseModule
        )
    }
}