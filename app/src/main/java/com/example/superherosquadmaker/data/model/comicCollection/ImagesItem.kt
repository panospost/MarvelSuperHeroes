package com.example.superherosquadmaker.data.model.comicCollection

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesItem(val path: String = "",
                      val extension: String = "")