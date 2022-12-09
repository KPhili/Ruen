package com.example.data.repositories

import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.data.configuration.IMAGE_DIRECTORY
import com.example.data.datasource.ImageRemoteSource
import com.example.domain.repositories.IImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ImageRepository(
    private val imageDownloader: ImageRemoteSource,
    private val downloadManager: DownloadManager,
    private val context: Context,
) : IImageRepository {

    override suspend fun downloadImage(url: String) = withContext(Dispatchers.IO) {
        val uri = Uri.parse(url)
        val dmId = imageDownloader.download(uri)
        val query = Query().setFilterById(dmId).setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)
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
        return@withContext fileName ?: throw Exception("Error with download image $url")
    }

    override suspend fun deleteImage(imageFileName: String?) = withContext(Dispatchers.IO) {
        if(imageFileName == null) return@withContext -1
        val files = context.getExternalFilesDir(IMAGE_DIRECTORY)
            ?.listFiles { _, fileName -> fileName == imageFileName }
        if (files.isNullOrEmpty()) {
            Log.d(TAG, "deleteImage: image file not exists $imageFileName")
        } else {
            files.forEach { it.delete() }
            Log.d(TAG, "deleteImage: image file delete success $imageFileName")
        }
    }

    companion object {
        private val MAX_ATTEMPTS_COUNT = 10
        private const val TAG = "ImageRepository"
    }
}