package com.example.superherosquadmaker.ui.shared

import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.data.localdb.HerosLocalDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalDbRepository(private val db: HerosLocalDb) {

     fun getMySquad(): Flow<List<Hero>> =
        flow { emit(db.heroesDao.getMySquad(1)) }

    fun removeOrAddToMySquad(hero: Hero) = flow {
        db.heroesDao.removeOrAddToMySquad(hero)
        emit(Unit)
    }

    fun insertAll(heroes: List<Hero>) = flow {
        db.heroesDao.insertAll(heroes)
        emit(Unit)
    }

    fun insertAllComics(comics: List<ComicLocal>) = flow {
        db.heroesDao.insertAllComics(comics)
        emit(Unit)
    }

    fun getComicsOfAHero(heroId: Int): Flow<List<ComicLocal>> = flow{
        emit(db.heroesDao.getComicsOfSpecificHero(heroId))
    }

    fun getAllHeroes(): Flow<List<Hero>> = flow {
        emit(db.heroesDao.getAllHeroes())
    }

    fun clear() = flow {
        db.heroesDao.clear()
        emit(Unit)
    }

}