package com.example.superherosquadmaker.ui.shared

import com.example.superherosquadmaker.BuildConfig
import com.example.superherosquadmaker.data.api.APIInterface
import com.example.superherosquadmaker.data.model.comicCollection.ComicsData
import com.example.superherosquadmaker.data.model.heroesCollection.HeroesData
import com.example.superherosquadmaker.utils.md5
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class MarverRepository( private val apiInterface: APIInterface) {
    private val ORDER_BY_NAME = "name"
    private val ORDER_BY_TITLE = "title"
    private val FORMAT = "comic"

    fun getAllHeroes(
        limit: Int
    ): Flow<HeroesData?> = flow {
        val timestamp = Calendar.getInstance().time.toString()

        emit(
            apiInterface.getAllHeroes(
                ORDER_BY_NAME, limit, timestamp,
                BuildConfig.PUBLIC_KEY,
                getHash(timestamp)
            ).data
        )
    }

    fun getMoreHeroes(
        limit: Int,
        offset: Int
    ): Flow<HeroesData?> = flow {
        val timestamp = Calendar.getInstance().time.toString()

        emit(
            apiInterface.getMoreHeroes(
                ORDER_BY_NAME, limit,offset, timestamp,
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

    suspend fun getHeroByName(name: String)= flow {
        val timestamp = Calendar.getInstance().time.toString()
        emit(
            apiInterface.getHeroByName(
                name,
                timestamp,
                BuildConfig.PUBLIC_KEY,
                getHash(timestamp)
            ).data
        )
    }

    /**
     * needed by the marvel api for non web applications
     * to has the public key with the private key and a timestamp
     */
    private fun getHash(timestamp: String): String {
        val hash =
            timestamp + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY
        return hash.md5()
    }


}