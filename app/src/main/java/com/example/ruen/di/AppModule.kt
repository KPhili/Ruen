package com.example.ruen.di

import com.example.data.datasource.LibreWordTranslationRemoteSource
import com.example.data.repositories.WordTranslationRepository
import com.example.ruen.viewmodel.TranslatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::TranslatorViewModel)
    singleOf(::WordTranslationRepository)
    singleOf(::LibreWordTranslationRemoteSource)
}