package com.example.data.di

import android.app.DownloadManager
import android.content.Context
import com.example.data.datasource.ImageRemoteSource
import com.example.data.repositories.ImageRepository
import com.example.domain.repositories.IImageRepository
import org.koin.dsl.module

val dataModule = module {
    factory<IImageRepository> { ImageRepository(imageDownloader = get(), context = get()) }
    factory { ImageRemoteSource(context = get()) }
    factory { get<Context>().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }
}