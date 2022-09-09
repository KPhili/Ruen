package com.example.ruen.di

import com.example.data.datasource.LibreWordTranslationRemoteSource
import com.example.data.repositories.CardRepository
import com.example.data.repositories.TranslatedWordRepository
import com.example.domain.repositories.ICardRepository
import com.example.domain.repositories.ITranslatedWordRepository
import com.example.domain.usecases.SaveCardWithTranslatedWordUseCase
import com.example.ruen.viewmodel.TranslatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::TranslatorViewModel)
    singleOf(::TranslatedWordRepository) bind ITranslatedWordRepository::class
    singleOf(::LibreWordTranslationRemoteSource)
    factoryOf(::SaveCardWithTranslatedWordUseCase)
    singleOf(::CardRepository) bind ICardRepository::class
}