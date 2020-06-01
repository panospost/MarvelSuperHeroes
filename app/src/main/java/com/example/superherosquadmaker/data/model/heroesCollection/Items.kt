package com.example.superherosquadmaker.data.model.heroesCollection

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Items(
    val name: String = "",
    val resourceURI: String = "",
    val type: String? = ""
)