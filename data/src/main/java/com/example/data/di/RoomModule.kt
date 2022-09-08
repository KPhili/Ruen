package com.example.data.di

import androidx.room.Room
import com.example.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val roomModule = module{
    single{
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, get(named("DB_NAME")))
    }

    single(named("DB_NAME")){
        "RUEN.db"
    }
}