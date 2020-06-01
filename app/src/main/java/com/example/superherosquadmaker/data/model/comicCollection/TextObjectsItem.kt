package com.example.superherosquadmaker.data.model.comicCollection

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextObjectsItem(val language: String = "",
                           val text: String = "",
                           val type: String = "")