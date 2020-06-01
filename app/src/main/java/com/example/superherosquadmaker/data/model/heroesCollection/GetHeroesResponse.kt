package com.example.superherosquadmaker.data.model.heroesCollection

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GetHeroesResponse(val copyright: String = "",
                             val code: String = "",
                             val data: HeroesData,
                             val attributionHTML: String = "",
                             val attributionText: String = "",
                             val etag: String = "",
                             val status: String = "")