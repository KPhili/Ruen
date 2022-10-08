package com.example.ruen.notifications

import android.app.Notification

interface INotificationHelper {
    enum class NotificationType {
        REMIND_REPETITION, EMPTY
    }

    fun create(type: INotificationHelper.NotificationType): Notification

    fun sendNotification(type: NotificationType)
}