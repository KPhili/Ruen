package com.example.data.datasource

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import com.example.data.configuration.IMAGE_DIRECTORY

class ImageRemoteSource(private val context: Context) {
    fun download(uri: Uri): Long {
        val request = Request(uri).apply {
            setNotificationVisibility(Request.VISIBILITY_HIDDEN)
            setDestinationInExternalFilesDir(context, IMAGE_DIRECTORY, IMAGE_NAME)
        }
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return manager.enqueue(request)
    }

    companion object {
        private const val IMAGE_NAME = "image.jpg"
    }
}