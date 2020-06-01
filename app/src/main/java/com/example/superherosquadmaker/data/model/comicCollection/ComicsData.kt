package com.example.superherosquadmaker.data.model.comicCollection

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ComicsData(val total: String = "",
                      val offset: String = "",
                      val limit: String = "",
                      val count: String = "",
                      val results: List<Comic>?)