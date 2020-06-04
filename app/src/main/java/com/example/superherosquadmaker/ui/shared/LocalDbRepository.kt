package com.example.superherosquadmaker.ui.shared

import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.data.localdb.Marveldao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalDbRepository(private val heroesDao: Marveldao) {

     fun getMySquad(): Flow<List<Hero>> =
        flow { emit(heroesDao.getMySquad(1)) }

    fun removeOrAddToMySquad(hero: Hero) = flow {
        heroesDao.removeOrAddToMySquad(hero)
        emit(Unit)
    }

    fun insertAll(heroes: List<Hero>) = flow {
        heroesDao.insertAll(heroes)
        emit(Unit)
    }

    fun insertAllComics(comics: List<ComicLocal>) = flow {
        heroesDao.insertAllComics(comics)
        emit(Unit)
    }

    fun getComicsOfAHero(heroId: Int): Flow<List<ComicLocal>> = flow{
        emit(heroesDao.getComicsOfSpecificHero(heroId))
    }

    fun getAllHeroes(): Flow<List<Hero>> = flow {
        emit(heroesDao.getAllHeroes())
    }

    fun getHeroByName(name: String): Flow<Hero?> = flow {
        emit(heroesDao.getHeroByName(name))
    }
}