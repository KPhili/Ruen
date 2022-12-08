package com.example.data.repositories

import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.database.ContentObserver
import android.net.Uri
import com.example.data.datasource.ImageDownloader
import com.example.domain.repositories.IImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

class ImageRepository(
    private val imageDownloader: ImageDownloader,
    private val downloadManager: DownloadManager
) : IImageRepository {

    override suspend fun downloadImage(url: String) = withContext(Dispatchers.IO) {
        val uri = Uri.parse(url)
        val dmId = imageDownloader.download(uri)
        val query = Query().setFilterById(dmId).setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)
        var cursorUri: String? = null
        var counter = MAX_ATTEMPTS_COUNT
        while (cursorUri == null && counter-- != 0){
            val cursor = downloadManager.query(query)
            if(cursor.moveToFirst()){
                cursorUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            } else{
                delay(100)
            }
        }
        return@withContext cursorUri ?: throw Exception("Error with download image $url")
    }

    companion object {
        private val MAX_ATTEMPTS_COUNT = 10
    }
}