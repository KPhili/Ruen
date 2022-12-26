package com.example.data.datasource

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.delay
import java.io.File
import java.time.Instant

class ImageRemoteSource(private val context: Context) {
    suspend fun download(uri: Uri): String {
        val request = Request(uri).apply {
            setNotificationVisibility(Request.VISIBILITY_HIDDEN)
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, IMAGE_NAME)

        }
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val dmId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(dmId)
            .setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)
        var fileName: String? = null
        var counter = MAX_ATTEMPTS_COUNT
        while (fileName == null && counter-- != 0) {
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
            } else {
                delay(100)
            }
        }
        fileName?.let {
            val file = context.getExternalFilesDir("${Environment.DIRECTORY_DOWNLOADS}/$it")
            fileName = "image-${Instant.now().toEpochMilli()}.jpg"
            val newFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
            file?.renameTo(newFile)
        }
        return fileName ?: throw Exception("Error with download image $uri")
    }

    companion object {
        private val MAX_ATTEMPTS_COUNT = 10
        private const val IMAGE_NAME = "image.jpg"
    }
}