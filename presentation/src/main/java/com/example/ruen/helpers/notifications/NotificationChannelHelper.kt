package com.example.ruen.helpers.notifications

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ruen.R

class NotificationChannelHelper(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
) : INotificationChannelHelper {

    override fun createChannels(type: INotificationChannelHelper.NotificationChannelType) {
        val channel = create(type)
        notificationManager.createNotificationChannel(channel)
    }

    private fun create(type: INotificationChannelHelper.NotificationChannelType) = when (type) {
        INotificationChannelHelper.NotificationChannelType.DEFAULT -> createDefaultChannel()
    }

    private fun createDefaultChannel() = NotificationChannelCompat.Builder(
        REMIND_REPETITION_CHANNEL_ID,
        NotificationManagerCompat.IMPORTANCE_DEFAULT
    )
        .setName(context.getString(R.string.notification_default_channel_name))
        .setDescription(context.getString(R.string.notification_default_channel_description))
        .build()

    companion object {
        const val REMIND_REPETITION_CHANNEL_ID = "Remind repetition"
    }
}