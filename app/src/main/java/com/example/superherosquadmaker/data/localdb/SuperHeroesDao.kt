package com.example.superherosquadmaker.data.localdb

import androidx.room.*
//import androidx.paging.DataSource

@Dao
interface Marveldao{

    @Update
    fun removeOrAddToMySquad(hero: Hero)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(heroes: List<Hero>)

    @Query(value = "SELECT * FROM  superheroes WHERE isInMySquad IN (:inSquad)")
    fun getMySquad(inSquad: Int): List<Hero>

    @Query(value = "SELECT * FROM  superheroes ORDER BY name")
    fun getAllHeroes(): List<Hero>

    @Query(value = "DELETE from superheroes")
    fun clear()

////    @Query("SELECT * FROM superheroes_local_db")
////    fun getAllRecipes(): DataSource.Factory<Int, Hero>
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllComics(comics: List<ComicLocal>)

    @Query(value = "SELECT * FROM  comicsDb WHERE characterId=:heroId")
    fun getComicsOfSpecificHero(heroId: Int): List<ComicLocal>

}