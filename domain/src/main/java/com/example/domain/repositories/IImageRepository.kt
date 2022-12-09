package com.example.domain.repositories

interface IImageRepository {
    suspend fun downloadImage(url: String): String
    suspend fun deleteImage(uri: String?): Int
}