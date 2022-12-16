package com.example.ruen.helpers.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerHelper(
    private val context: Context,
    private val workManager: WorkManager = WorkManager.getInstance(context)
) : IWorkManagerHelper {

    override fun enableRepeatNotificationWorker(startTimeHours: Int, endTimeHours:Int) {
        val data = Data.Builder()
        data.putInt(NotificationWorker.START_TIME, startTimeHours)
        data.putInt(NotificationWorker.END_TIME, endTimeHours)
        val request =
            PeriodicWorkRequestBuilder<NotificationWorker>(REPEAT_INTERVAL_HOURS, TimeUnit.HOURS)
                .setInitialDelay(REPEAT_DELAY_HOURS, TimeUnit.HOURS)
                .addTag(REPEAT_WORKER_TAG)
                .setInputData(data.build())
                .build()

        workManager.enqueue(request)
        Log.d(TAG, "enableRepeatNotificationWorker: worker added to scheduler")
    }

    override fun cancelRepeatNotificationWorker() {
        workManager.cancelAllWorkByTag(REPEAT_WORKER_TAG)
        Log.d(TAG, "cancelRepeatNotificationWorker: worker removed from scheduler")
    }

    companion object {
        private const val REPEAT_INTERVAL_HOURS = 1L
        private const val REPEAT_DELAY_HOURS = 1L
        private const val TAG = "WorkManagerHelper"
        private const val REPEAT_WORKER_TAG = "REPEAT WORKER TAG"
    }
}