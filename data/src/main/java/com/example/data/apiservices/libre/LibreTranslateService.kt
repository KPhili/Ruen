package com.example.data.apiservices.libre

import com.example.data.models.LibreTranslatedWord
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LibreTranslateService {
    @Headers("origin:https://libretranslate.com", "Content-Type: application/json")
    @POST("translate")
    suspend fun translate(
        @Body
        translateQueryBody: LibreTranslateQueryBody
    ): LibreTranslatedWord
}
