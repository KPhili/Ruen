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
import com.example.domain.usecases.*
import com.example.ruen.adapters.CardsAdapter
import com.example.ruen.adapters.CardsAdapter.CardComparator
import com.example.ruen.adapters.GroupsAdapter
import com.example.ruen.helpers.notifications.INotificationChannelHelper
import com.example.ruen.helpers.notifications.INotificationHelper
import com.example.ruen.helpers.notifications.NotificationChannelHelper
import com.example.ruen.helpers.notifications.NotificationHelper
import com.example.ruen.helpers.workmanager.IWorkManagerHelper
import com.example.ruen.helpers.workmanager.WorkManagerHelper
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
        CardViewModel(
            translatedWordRepository = get(),
            cardRepository = get(),
            saveCardWithTranslatedWordUseCase = get(),
            updateCardWithTranslatedWordUseCase = get(),
            resourceProvider = get(),
            internetConnectionChecker = get(),
            groupId = parameters[0],
            cardId = parameters[1]
        )
    }
    viewModel { parameters -> CardsViewModel(cardsRepository = get(), groupId = parameters.get()) }
    viewModel { parameters ->
        CardRepeatViewModel(
            cardRepository = get(),
            formatRepeatIntervalUseCase = get(),
            getNextRepeatNumberUseCase = get(),
            getIntervalRepeatUseCase = get(),
            groupId = parameters[0]
        )
    }
    viewModelOf(::GroupsViewModel)
    viewModel { parameters -> GroupViewModel(groupRepository = get(), groupId = parameters[0]) }

    singleOf(::LibreWordTranslationRemoteSource)

    // repositories
    singleOf(::CardRepository) bind ICardRepository::class
    singleOf(::GroupRepository) bind IGroupRepository::class
    singleOf(::TranslatedWordRepository) bind ITranslatedWordRepository::class

    singleOf(::ResourceProvider) bind IResourceProvider::class

    // adapters
    factory { GroupsAdapter(get(named("GroupComparator"))) }
    factory { CardsAdapter(get(named("CardComparator"))) }

    // diff utils for adapters
    factory<DiffUtil.ItemCallback<Card>>(named("CardComparator")) { CardComparator }
    factory<DiffUtil.ItemCallback<Group>>(named("GroupComparator")) { GroupsAdapter.GroupComparator }

    // usecases
    factoryOf(::FormatRepeatIntervalUseCase)
    factoryOf(::GetNextRepeatNumberUseCase)
    factoryOf(::GetIntervalRepeatUseCase)
    factoryOf(::SaveCardWithTranslatedWordUseCase)
    factoryOf(::UpdateCardWithTranslatedWordUseCase)

    // utils
    singleOf(::InternetConnectionChecker)

    // notifications
    factory { NotificationChannelHelper(get()) } bind INotificationChannelHelper::class
    factory { NotificationHelper(get()) } bind INotificationHelper::class

    // workmanagerhelper
    factory { WorkManagerHelper(get()) } bind IWorkManagerHelper::class

}