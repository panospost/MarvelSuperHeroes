package com.example.superherosquadmaker.data.model.heroesCollection

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeroesData(val total: Int,
                      val offset: Int,
                      val limit: Int,
                      val count: Int,
                      val results: List<SuperHeroes>?)