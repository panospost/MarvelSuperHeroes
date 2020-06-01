package com.example.superherosquadmaker.data.model.comicCollection

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Series(val name: String = "",
                  val resourceURI: String = "")