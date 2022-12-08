package com.example.domain.repositories

interface IImageRepository {
    suspend fun downloadImage(url: String): String
}