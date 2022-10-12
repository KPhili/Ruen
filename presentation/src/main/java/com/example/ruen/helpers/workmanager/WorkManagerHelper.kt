package com.example.ruen.helpers.workmanager

import android.content.Context
import android.util.Log
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.*
import java.util.concurrent.TimeUnit

class WorkManagerHelper(
    private val context: Context,
    private val workManager: WorkManager = WorkManager.getInstance(context)
) : IWorkManagerHelper {

    override fun enableRepeatNotificationWorker() {
        val request =
            PeriodicWorkRequestBuilder<NotificationWorker>(REPEAT_INTERVAL_HOURS, TimeUnit.HOURS)
                .setInitialDelay(getInterval(), TimeUnit.MILLISECONDS)
                .addTag(REPEAT_WORKER_TAG)
                .build()

        workManager.enqueue(request)
        Log.i(TAG, "enableRepeatNotificationWorker: worker added to scheduler")
    }

    override fun cancelRepeatNotificationWorker() {
        workManager.cancelAllWorkByTag(REPEAT_WORKER_TAG)
    }

    private fun getInterval(): Long {
        val now = Calendar.getInstance()
        val future = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, REPEAT_HOUR)
            set(Calendar.AM_PM, Calendar.AM)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (future < now) {
            future.add(Calendar.DATE, 1)
        }
        return future.timeInMillis - now.timeInMillis
    }

    companion object {
        private const val REPEAT_INTERVAL_HOURS = 12L
        private const val REPEAT_HOUR = 14
        private const val TAG = "WorkManagerHelper"
        private const val REPEAT_WORKER_TAG = "REPEAT WORKER TAG"
    }
}