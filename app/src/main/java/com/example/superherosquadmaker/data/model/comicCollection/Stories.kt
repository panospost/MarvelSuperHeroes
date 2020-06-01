package com.example.superherosquadmaker.data.model.comicCollection

import com.example.superherosquadmaker.data.model.heroesCollection.Items

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stories(val collectionURI: String = "",
                   val available: Int = 0,
                   val returned: Int = 0,
                   val items: List<Items>?)