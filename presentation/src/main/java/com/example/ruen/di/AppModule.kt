package com.example.ruen.di

import androidx.recyclerview.widget.DiffUtil
import com.example.data.datasource.LibreWordTranslationRemoteSource
import com.example.data.repositories.CardRepository
import com.example.data.repositories.TranslatedWordRepository
import com.example.domain.models.Card
import com.example.domain.providers.IResourceProvider
import com.example.domain.repositories.ICardRepository
import com.example.domain.repositories.ITranslatedWordRepository
import com.example.domain.usecases.FormatRepeatIntervalUseCase
import com.example.domain.usecases.GetIntervalRepeatUseCase
import com.example.domain.usecases.GetNextRepeatNumberUseCase
import com.example.domain.usecases.SaveCardWithTranslatedWordUseCase
import com.example.ruen.adapters.CardsAdapter
import com.example.ruen.adapters.CardsAdapter.CardComparator
import com.example.ruen.providers.ResourceProvider
import com.example.ruen.utils.InternetConnectionChecker
import com.example.ruen.viewmodels.CardRepeatViewModel
import com.example.ruen.viewmodels.CardsViewModel
import com.example.ruen.viewmodels.TranslatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::TranslatorViewModel)
    viewModelOf(::CardsViewModel)
    viewModelOf(::CardRepeatViewModel)
    singleOf(::TranslatedWordRepository) bind ITranslatedWordRepository::class
    singleOf(::LibreWordTranslationRemoteSource)
    factoryOf(::SaveCardWithTranslatedWordUseCase)
    singleOf(::CardRepository) bind ICardRepository::class
    singleOf(::ResourceProvider) bind IResourceProvider::class
    factoryOf(::CardsAdapter)
    factory<DiffUtil.ItemCallback<Card>> { CardComparator }
    factoryOf(::FormatRepeatIntervalUseCase)
    singleOf(::InternetConnectionChecker)
    factoryOf(::GetNextRepeatNumberUseCase)
    factoryOf(::GetIntervalRepeatUseCase)

}