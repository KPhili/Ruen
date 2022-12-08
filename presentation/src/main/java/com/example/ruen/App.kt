package com.example.ruen

import android.app.Application
import com.example.data.di.dataModule
import com.example.data.di.roomModule
import com.example.ruen.di.appModule
import com.example.ruen.di.retrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule, retrofitModule, roomModule, dataModule)
            androidLogger()
            androidContext(this@App)
        }
    }
}