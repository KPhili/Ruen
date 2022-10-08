package com.example.ruen.notifications

interface INotificationChannelHelper {
    fun createChannels(type: NotificationChannelType)
    enum class NotificationChannelType {
        DEFAULT
    }
}