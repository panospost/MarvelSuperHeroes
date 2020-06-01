package com.example.superherosquadmaker.utils

import com.example.superherosquadmaker.data.model.Thumbnail



fun completeBuildOfURL(thumbnail: Thumbnail, portrait: PortraitSizes): String {
    return getHttpsUrl(thumbnail.path + "/" + portrait.size + "." + thumbnail.extension)
}

//url needs to be https otherwise the image is not showing
fun getHttpsUrl(imageUrl: String): String {
    return imageUrl.substring(0, 4) + "s" + imageUrl.substring(4, imageUrl.length)
}

enum class PortraitSizes(val size: String){
    LARGE("portrait_xlarge"),
    SMALL("portrait_xsmall"),
    PORTRAIT("portrait_uncanny"),
    MEDIUM("portrait_xmedium"),
    SQUARE_LARGE("standard_fantastic")
}