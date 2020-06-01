package com.example.superherosquadmaker.utils

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.data.model.comicCollection.Comic
import com.example.superherosquadmaker.data.model.heroesCollection.SuperHeroes
import java.math.BigInteger
import java.security.MessageDigest

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun SuperHeroes.fromHeroToSuperHero() =
    Hero(
        id = id,
        name = name.trim(),
        description = if(description!! == "") "No description was given" else description,
        thumbnail = completeBuildOfURL(
            thumbnail,
            PortraitSizes.SQUARE_LARGE
        ),
        totalComics = comics.items?.size
    )

fun Comic.fromMarvelComicToLocalComic(heroId: Int) = ComicLocal(
    id = id,
    title = title,
    thumbnail = completeBuildOfURL(thumbnail, PortraitSizes.SQUARE_LARGE),
    characterId = heroId
)

fun Hero.copyWithNewSquadStatus(newstatus: Int) = Hero(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail,
    totalComics = totalComics,
    isInMySquad = newstatus
)

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL
    )
    val drawable = ContextCompat.getDrawable(
        this.context,
        drawableRes
    )
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}