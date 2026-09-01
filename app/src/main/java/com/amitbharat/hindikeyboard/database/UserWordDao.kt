package com.amitbharat.hindikeyboard.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWordDao {
    @Query("SELECT * FROM user_words WHERE language = :lang ORDER BY frequency DESC, lastUsed DESC LIMIT 100")
    fun getTopWords(lang: String): Flow<List<UserWordEntity>>

    @Query("SELECT * FROM user_words WHERE word LIKE :prefix || '%' AND language = :lang ORDER BY frequency DESC LIMIT 10")
    suspend fun getMatchingWords(prefix: String, lang: String): List<UserWordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(word: UserWordEntity)

    @Query("UPDATE user_words SET frequency = frequency + 1, lastUsed = :timestamp WHERE word = :word AND language = :lang")
    suspend fun incrementFrequency(word: String, lang: String, timestamp: Long = System.currentTimeMillis())

    @Delete
    suspend fun delete(word: UserWordEntity)
}
