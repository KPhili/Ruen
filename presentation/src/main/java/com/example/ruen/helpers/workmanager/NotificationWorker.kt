package com.example.ruen.helpers.workmanager

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.domain.repositories.ICardRepository
import com.example.ruen.R
import com.example.ruen.helpers.notifications.INotificationChannelHelper
import com.example.ruen.helpers.notifications.INotificationHelper
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val cardRepository: ICardRepository by inject()
    private val notification: INotificationHelper by inject()

    override suspend fun doWork(): Result {

        if (cardRepository.isExistForRepeat()) {
            notification.sendNotification(INotificationHelper.NotificationType.REMIND_REPETITION)
        }
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID,
            notification.create(INotificationHelper.NotificationType.EMPTY)
        )
    }

    companion object {
        private const val NOTIFICATION_ID = 1122
    }
}