package com.example.superherosquadmaker.ui.shared

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.data.localdb.Marveldao
import com.example.superherosquadmaker.utils.TestCoroutineRule
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalDbRepositoryTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var dao: Marveldao


    private lateinit var repository: LocalDbRepository

    @Before
    fun setup() {
        repository = LocalDbRepository(dao)
    }

    @Test
    fun getMySquadTest_() {
        val hero = Hero(1, "a", "hero", "url", 1, 1)

        Mockito.doReturn(
            listOf(hero)
        ).`when`(dao).getMySquad(1)
        val x = repository.getMySquad()
        assertNotNull(x)
    }

    @Test
    fun getAllHeroesTest_() {
        val hero = Hero(1, "a", "hero", "url", 1, 1)

        Mockito.doReturn(
            listOf(hero)
        ).`when`(dao).getAllHeroes()
        val x = repository.getAllHeroes()
        assertNotNull(x)
    }

    @Test
    fun getHeroByName() {
        val hero = Hero(1, "a", "hero", "url", 1, 1)

        Mockito.doReturn(
           hero
        ).`when`(dao).getHeroByName(hero.name)
        val x = repository.getHeroByName(hero.name)
        assertNotNull(x)
    }

    @Test
    fun getComicsOfAHeroTest() {
        val comic =  ComicLocal(1, "a", "hero", 1)

        Mockito.doReturn(
           listOf(comic)
        ).`when`(dao).getComicsOfSpecificHero(1)
        val x = repository.getComicsOfAHero(1)
        assertNotNull(x)
    }
}