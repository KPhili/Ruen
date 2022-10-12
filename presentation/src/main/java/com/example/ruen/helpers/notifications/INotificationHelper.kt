package com.example.ruen.helpers.notifications

import android.app.Notification

interface INotificationHelper {
    enum class NotificationType {
        REMIND_REPETITION, EMPTY
    }

    fun create(type: NotificationType): Notification

    fun sendNotification(type: NotificationType)
}