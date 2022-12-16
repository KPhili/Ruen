package com.example.ruen.helpers.workmanager

interface IWorkManagerHelper {
    fun cancelRepeatNotificationWorker()
    fun enableRepeatNotificationWorker(startTimeHours: Int, endTimeHours: Int)
}