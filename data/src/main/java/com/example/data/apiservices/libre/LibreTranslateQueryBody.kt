package com.example.data.apiservices.libre

data class LibreTranslateQueryBody(
    val q: String,
    val target: String = "ru",
    val source: String = "en",
    val format: String = "text",
    val api_key: String = ""
)