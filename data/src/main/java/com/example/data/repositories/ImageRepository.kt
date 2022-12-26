package com.example.data.repositories

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.data.datasource.ImageRemoteSource
import com.example.domain.repositories.IImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepository(
    private val imageDownloader: ImageRemoteSource,
    private val context: Context,
) : IImageRepository {

    override suspend fun downloadImage(url: String) = withContext(Dispatchers.IO) {
        val uri = Uri.parse(url)
        return@withContext imageDownloader.download(uri)
    }

    override suspend fun deleteImage(imageFileName: String?) = withContext(Dispatchers.IO) {
        if(imageFileName == null) return@withContext -1
        val files = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?.listFiles { _, fileName -> fileName == imageFileName }
        if (files.isNullOrEmpty()) {
            Log.d(TAG, "deleteImage: image file not exists $imageFileName")
        } else {
            files.forEach { it.delete() }
            Log.d(TAG, "deleteImage: image file delete success $imageFileName")
        }
    }

    companion object {
        private const val TAG = "ImageRepository"
    }
}