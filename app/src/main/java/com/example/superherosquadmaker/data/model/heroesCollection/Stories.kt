package com.example.superherosquadmaker.data.model.heroesCollection

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Stories(val collectionURI: String = "",
                   val available: String = "",
                   val returned: String = "",
                    val items: List<Items>?)