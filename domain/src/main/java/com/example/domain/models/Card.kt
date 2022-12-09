package com.example.domain.models

import java.time.LocalDateTime

data class Card(
    val id: Long? = null,
    val value: String,
    val nextRepetition: LocalDateTime = LocalDateTime.now(),
    val repeatNumber: Int = 0,
    val groupId: Long,
    val imageFileName: String? = null,
    val imageUrl: String? = null,
)
