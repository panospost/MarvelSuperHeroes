package com.example.superherosquadmaker.data.model.comicCollection

import com.example.superherosquadmaker.data.model.heroesCollection.HeroesData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetComicResponse(val copyright: String = "",
                            val code: String = "",
                            val data: ComicsData,
                            val attributionHTML: String = "",
                            val attributionText: String = "",
                            val etag: String = "",
                            val status: String = "")