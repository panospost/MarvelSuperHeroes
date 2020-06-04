package com.example.superherosquadmaker.data.model.comicCollection

import com.example.superherosquadmaker.data.model.Thumbnail
import com.example.superherosquadmaker.data.model.UrlsItem

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comic(val creators: Creators,
                 val issueNumber: Double = 0.0,
                 val isbn: String = "",
                 val description: String? = "",
                 val title: String = "",
                 val diamondCode: String = "",
                 val characters: Characters,
                 val urls: List<UrlsItem>?,
                 val ean: String = "",
                 val modified: String = "",
                 val id: Int,
                 val prices: List<PricesItem>?,
                 val events: Events,
                 val pageCount: Int,
                 val thumbnail: Thumbnail,
                 val images: List<ImagesItem>?,
                 val stories: Stories,
                 val textObjects: List<TextObjectsItem>?,
                 val digitalId: Int = 0,
                 val format: String = "",
                 val upc: String = "",
                 val dates: List<DatesItem>?,
                 val resourceURI: String = "",
                 val variantDescription: String = "",
                 val issn: String = "",
                 val series: Series
)