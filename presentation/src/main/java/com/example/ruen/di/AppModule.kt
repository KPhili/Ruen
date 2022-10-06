package com.example.ruen.di

import androidx.recyclerview.widget.DiffUtil
import com.example.data.datasource.LibreWordTranslationRemoteSource
import com.example.data.repositories.CardRepository
import com.example.data.repositories.GroupRepository
import com.example.data.repositories.TranslatedWordRepository
import com.example.domain.models.Card
import com.example.domain.models.Group
import com.example.domain.providers.IResourceProvider
import com.example.domain.repositories.ICardRepository
import com.example.domain.repositories.IGroupRepository
import com.example.domain.repositories.ITranslatedWordRepository
import com.example.domain.usecases.FormatRepeatIntervalUseCase
import com.example.domain.usecases.GetIntervalRepeatUseCase
import com.example.domain.usecases.GetNextRepeatNumberUseCase
import com.example.domain.usecases.SaveCardWithTranslatedWordUseCase
import com.example.ruen.adapters.CardsAdapter
import com.example.ruen.adapters.CardsAdapter.CardComparator
import com.example.ruen.adapters.GroupsAdapter
import com.example.ruen.providers.ResourceProvider
import com.example.ruen.utils.InternetConnectionChecker
import com.example.ruen.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    // viewmodels
    viewModel { parameters ->
        NewCardViewModel(
            repository = get(),
            saveCardWithTranslatedWordUseCase = get(),
            resourceProvider = get(),
            internetConnectionChecker = get(),
            groupId = parameters.get()
        )
    }
    viewModelOf(::CardsViewModel)
    viewModelOf(::CardRepeatViewModel)
    viewModelOf(::GroupsViewModel)
    viewModelOf(::NewGroupViewModel)

    singleOf(::LibreWordTranslationRemoteSource)

    singleOf(::CardRepository) bind ICardRepository::class
    singleOf(::GroupRepository) bind IGroupRepository::class
    singleOf(::TranslatedWordRepository) bind ITranslatedWordRepository::class

    singleOf(::ResourceProvider) bind IResourceProvider::class

    factory { GroupsAdapter(get(named("GroupComparator"))) }
    factory { CardsAdapter(get(named("CardComparator"))) }

    // diff utils for adapters
    factory<DiffUtil.ItemCallback<Card>>(named("CardComparator")) { CardComparator }
    factory<DiffUtil.ItemCallback<Group>>(named("GroupComparator")) { GroupsAdapter.GroupComparator }

    factoryOf(::FormatRepeatIntervalUseCase)
    factoryOf(::GetNextRepeatNumberUseCase)
    factoryOf(::GetIntervalRepeatUseCase)
    factoryOf(::SaveCardWithTranslatedWordUseCase)

    singleOf(::InternetConnectionChecker)
}