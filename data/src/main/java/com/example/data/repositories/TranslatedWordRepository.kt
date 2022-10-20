package com.example.data.repositories

import com.example.data.datasource.LibreWordTranslationRemoteSource
import com.example.data.db.dao.TranslatedWordDao
import com.example.data.mappers.toTranslatedWord
import com.example.data.mappers.toTranslatedWordRoom
import com.example.domain.models.Card
import com.example.domain.models.TranslatedWord
import com.example.domain.repositories.ITranslatedWordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TranslatedWordRepository(
    private val remoteSource: LibreWordTranslationRemoteSource,
    private val translatedWordDao: TranslatedWordDao
) : ITranslatedWordRepository {
    override suspend fun translate(word: String): List<TranslatedWord> =
        withContext(Dispatchers.IO) {
            val translateWord = remoteSource.translate(word).toTranslatedWord()
            listOf(translateWord)
        }

    override suspend fun insert(translatedWord: TranslatedWord) = withContext(Dispatchers.IO) {
        translatedWordDao.insert(translatedWord.toTranslatedWordRoom())
    }

    override suspend fun insert(translatedWords: List<TranslatedWord>) =
        withContext(Dispatchers.IO) {
            translatedWords.map { it.toTranslatedWordRoom() }
                .let { list ->
                    translatedWordDao.insert(list)
                }
        }
    override suspend fun deleteAllBelongsCard(cardId: Long) =
        withContext(Dispatchers.IO){
            translatedWordDao.deleteAllBelongsCard(cardId)
        }
}




