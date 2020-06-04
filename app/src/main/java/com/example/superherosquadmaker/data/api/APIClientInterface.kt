package com.example.superherosquadmaker.data.api

import com.example.superherosquadmaker.data.model.comicCollection.GetComicResponse
import com.example.superherosquadmaker.data.model.heroesCollection.GetHeroesResponse
import retrofit2.http.*


interface APIInterface {
    @GET("/v1/public/characters")
    suspend fun getAllHeroes(
        @Query("orderBy") orderBy: String,
        @Query("limit") limit: Int,
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String
    ): GetHeroesResponse

    @GET("/v1/public/characters")
    suspend fun getMoreHeroes(
        @Query("orderBy") orderBy: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String
    ): GetHeroesResponse

    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getAllComicsOfThatHero(
        @Path("characterId") characterId: Int,
        @Query("format") format: String,
        @Query("orderBy") orderBy: String,
        @Query("limit") limit: Int,
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String
    ): GetComicResponse

    @GET("/v1/public/characters")
    suspend fun getHeroByName(
        @Query("name") name: String,
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String
    ): GetHeroesResponse
}