package com.example.superherosquadmaker.data.model.comicCollection

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PricesItem(val price: Double = 0.0,
                      val type: String = "")