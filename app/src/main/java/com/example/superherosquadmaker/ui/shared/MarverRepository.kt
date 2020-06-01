package com.example.superherosquadmaker.ui.shared

import android.content.Context
import com.example.superherosquadmaker.BuildConfig
import com.example.superherosquadmaker.data.api.APIClient
import com.example.superherosquadmaker.data.model.comicCollection.ComicsData
import com.example.superherosquadmaker.data.model.heroesCollection.HeroesData
import com.example.superherosquadmaker.utils.md5
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class MarverRepository(private val context: Context) {
    private val ORDER_BY_NAME = "name"
    private val ORDER_BY_TITLE = "title"
    private val FORMAT = "comic"
    private val apiInterface = APIClient.getRetrofit(context)

    fun getAllHeroes(
        offset: Int
    ): Flow<HeroesData?> = flow {
        val timestamp = Calendar.getInstance().time.toString()

        emit(
            apiInterface.getAllHeroes(
                ORDER_BY_NAME, offset, timestamp,
                BuildConfig.PUBLIC_KEY,
                getHash(timestamp)
            ).data
        )
    }

    suspend fun getAllComics(
        characterId: Int
    ): Flow<ComicsData?> = flow {
        val timestamp = Calendar.getInstance().time.toString()
        emit(
            apiInterface.getAllComicsOfThatHero(
                characterId,
                FORMAT,
                ORDER_BY_TITLE,
                10,
                timestamp,
                BuildConfig.PUBLIC_KEY,
                getHash(timestamp)
            ).data
        )
    }


    private fun getHash(timestamp: String): String {
        val hash =
            timestamp + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY
        return hash.md5()
    }
}