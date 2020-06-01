package com.example.superherosquadmaker.data.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UrlsItem(val type: String = "",
                    val url: String = "")