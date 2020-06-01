package com.example.superherosquadmaker.data.model.heroesCollection

import com.example.superherosquadmaker.data.model.Thumbnail
import com.example.superherosquadmaker.data.model.UrlsItem
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SuperHeroes(val urls: List<UrlsItem>?,
                       val thumbnail: Thumbnail,
                       val stories: Stories,
                       val series: CharacterSeries,
                       val comics: Comics,
                       val name: String,
                       val description: String?,
                       val modified: String,
                       val id: Int,
                       val resourceURI: String,
                       val events: Events
)