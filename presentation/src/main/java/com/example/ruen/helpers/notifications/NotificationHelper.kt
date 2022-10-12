package com.example.ruen.helpers.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ruen.R
import com.example.ruen.helpers.notifications.NotificationChannelHelper.Companion.REMIND_REPETITION_CHANNEL_ID
import com.example.ruen.views.MainActivity

class NotificationHelper(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(
        context
    )
) : INotificationHelper {

    override fun sendNotification(type: INotificationHelper.NotificationType) {
        val notification = create(INotificationHelper.NotificationType.REMIND_REPETITION)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }


    override fun create(type: INotificationHelper.NotificationType) =
        when (type) {
            INotificationHelper.NotificationType.REMIND_REPETITION -> getRepeatNotification()
            INotificationHelper.NotificationType.EMPTY -> getEmptyNotification()
        }

    private fun getRepeatNotification() =
        NotificationCompat.Builder(context, REMIND_REPETITION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.time_to_repeat_the_word))
            .setSmallIcon(R.drawable.ic_launcher_foreground).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(getRepeatFragmentPendingIntent()).build()

    private fun getEmptyNotification() =
        NotificationCompat.Builder(context, REMIND_REPETITION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

    private fun getRepeatFragmentPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(context, REPEAT_FRAGMENT_REQUEST_CODE, intent, flags)
    }

    companion object {
        private const val REPEAT_FRAGMENT_REQUEST_CODE = 123
        private const val NOTIFICATION_ID = 1234
    }
}