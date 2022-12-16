package com.example.ruen.helpers.workmanager

import android.content.Context
import android.util.Log
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
import java.util.Calendar

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val cardRepository: ICardRepository by inject()
    private val notification: INotificationHelper by inject()

    override suspend fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR)
        val startTime = inputData.getInt(START_TIME, 10)
        val endTime = inputData.getInt(END_TIME, 20)
        if (currentHour in (startTime until endTime)) {
            if (cardRepository.isExistForRepeat()) {
                notification.sendNotification(INotificationHelper.NotificationType.REMIND_REPETITION)
                Log.d(TAG, "doWork: send notification")
            } else {
                Log.d(TAG, "doWork: no word for repetition")
            }
        } else {
            Log.d(TAG, "doWork: at this time, you need to rest, not repeat!")
        }
        Log.d(TAG, "Work finish success")
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
        private const val TAG = "NotificationWorker"
        const val START_TIME = "start_time"
        const val END_TIME = "end_time"
    }
}