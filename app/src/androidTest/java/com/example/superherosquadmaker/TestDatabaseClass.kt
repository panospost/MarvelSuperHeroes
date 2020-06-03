package com.example.superherosquadmaker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.data.localdb.HerosLocalDb
import com.example.superherosquadmaker.data.localdb.Marveldao
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var heroDao: Marveldao
    private lateinit var db: HerosLocalDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HerosLocalDb::class.java).build()
        heroDao = db.heroesDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadHeroes() {
        val hero = Hero(1,"1", "2", "3", 2, 0)
        val hero2 = Hero(2,"1", "2", "3", 2, 1)


        heroDao.insertAll(listOf(hero,hero2))
        val getAllHeroes = heroDao.getAllHeroes()
        assertThat(getAllHeroes, equalTo(listOf(hero,hero2)))
    }

    @Test
    @Throws(Exception::class)
    fun getMySquad() {
        val hero = Hero(1,"1", "2", "3", 2, 0)
        val hero2 = Hero(2,"1", "2", "3", 2, 1)


        heroDao.insertAll(listOf(hero,hero2))
        val getAllHeroes = heroDao.getMySquad(1)
        assertThat(getAllHeroes, equalTo(listOf(hero2)))
    }

    @Test
    @Throws(Exception::class)
    fun removeFromSquad() {
        val hero = Hero(1,"1", "2", "3", 2, 0)
        val hero2 = Hero(2,"1", "2", "3", 2, 1)
        val expectedhero = Hero(2,"1", "2", "3", 2, 0)


        heroDao.insertAll(listOf(hero,hero2))
        val getAllHeroes = heroDao.getAllHeroes()
        assertThat(getAllHeroes, equalTo(listOf(hero,expectedhero)))
    }

    @Test
    @Throws(Exception::class)
    fun insertAllComics() {
        val comic = ComicLocal(1,"1", "2", 2)
        val hero = Hero(2,"1", "2", "3", 2, 1)



        heroDao.insertAllComics(listOf(comic))
        val comics = heroDao.getComicsOfSpecificHero(hero.id)
        assertThat(comics, equalTo(listOf(comic)))
    }
}