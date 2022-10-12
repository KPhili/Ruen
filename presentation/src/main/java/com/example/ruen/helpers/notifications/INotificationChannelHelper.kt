package com.example.ruen.helpers.notifications

interface INotificationChannelHelper {
    fun createChannels(type: NotificationChannelType)
    enum class NotificationChannelType {
        DEFAULT
    }
}