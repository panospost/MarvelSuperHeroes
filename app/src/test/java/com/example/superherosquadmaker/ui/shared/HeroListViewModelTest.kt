package com.example.superherosquadmaker.ui.shared

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.utils.Resource
import com.example.superherosquadmaker.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HeroListViewModelTest{

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var marverRepository: MarverRepository

    @Mock
    private lateinit var repository: LocalDbRepository

    @Mock
    private lateinit var herosObserver: Observer<Resource<List<Hero>>>

    @Mock
    private lateinit var comicObs: Observer<Resource<List<ComicLocal>>>

    @Mock
    private lateinit var loadingMore: Observer<Resource<Boolean>>

    @Before
    fun setup() {

    }

    @Test
    fun getMySquadTest_Success() {
        testCoroutineRule.runBlockingTest {
            doReturn(flowOf(emptyList<Hero>()))
                .`when`(repository)
                .getMySquad()
            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.mySquad.observeForever(herosObserver)
            verify(repository).getMySquad()
            verify(herosObserver).onChanged(Resource.success(emptyList()))
            viewModel.mySquad.removeObserver(herosObserver)
        }
    }

    @Test
    fun getMySquadTest_Error() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message For You"
            doReturn(flow<List<Hero>> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(repository)
                .getMySquad()
            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.mySquad.observeForever(herosObserver)
            verify(repository).getMySquad()
            verify(herosObserver).onChanged(Resource.error(errorMessage, null))
            viewModel.mySquad.removeObserver(herosObserver)

        }
    }

    @Test
    fun updateCharacter_Success() {
        testCoroutineRule.runBlockingTest {
            val hero =  Hero(1, "a", "hero","url", 1, 1)

            doReturn(flowOf(listOf(hero)))
                .`when`(repository)
                .getMySquad()

            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.updateCharacter(hero)
            verify(repository).getMySquad()
            viewModel.mySquad.observeForever(herosObserver)
            verify(herosObserver).onChanged(Resource.success(listOf(hero)))
            viewModel.mySquad.removeObserver(herosObserver)

        }
    }

    @Test
    fun getHeroesNoHeroesInRepo_Success() {
        testCoroutineRule.runBlockingTest{
            doReturn(flowOf(emptyList<Hero>()))
                .`when`(repository)
                .getAllHeroes()

            doReturn(flowOf(emptyList<Hero>()))
                .`when`(marverRepository)
                .getAllHeroes(10)

            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.getHeroes()
            viewModel.heroes.observeForever(herosObserver)
            verify(herosObserver).onChanged(Resource.loading(null))
            viewModel.heroes.removeObserver(herosObserver)
        }
    }

    @Test
    fun getHeroesNoHeroesInRepo_Error() {
        testCoroutineRule.runBlockingTest{
            val errorMessage = "Your error message"
            doReturn(flow<Hero>{
                throw IllegalStateException(errorMessage)
            })
                .`when`(repository)
                .getAllHeroes()

            doReturn(flowOf(emptyList<Hero>()))
                .`when`(marverRepository)
                .getAllHeroes(10)

            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.getHeroes()
            viewModel.heroes.observeForever(herosObserver)
            verify(herosObserver).onChanged(Resource.error(errorMessage, null))
            viewModel.heroes.removeObserver(herosObserver)
        }
    }

    @Test
    fun getHeroesInRepo_Success() {
        testCoroutineRule.runBlockingTest{
            val hero =  Hero(1, "a", "hero","url", 1, 1)
            doReturn(flowOf(listOf(hero)))
                .`when`(repository)
                .getAllHeroes()

            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.getHeroes()
            viewModel.heroes.observeForever(herosObserver)
            verify(herosObserver).onChanged(Resource.loading(null))
            viewModel.heroes.removeObserver(herosObserver)
        }
    }

    @Test
    fun getMoreHeroes_Error() {
        testCoroutineRule.runBlockingTest{
            val errorMessage = "Your error message"
            doReturn(flow<Hero>{
                throw IllegalStateException(errorMessage)
            })
                .`when`(marverRepository)
                .getMoreHeroes(20, 0)

            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.getMoreHeroes()
            viewModel.loadingMore.observeForever(loadingMore)
            verify(loadingMore).onChanged(Resource.error(errorMessage, null))
            viewModel.loadingMore.removeObserver(loadingMore)
        }
    }

    @Test
    fun getComics_success() {
        testCoroutineRule.runBlockingTest{
            val comic =  ComicLocal(1, "a", "hero", 1)

            doReturn(flowOf(listOf(comic)))
                .`when`(repository)
                .getComicsOfAHero(1)

            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.getComics(1)
            viewModel.comics.observeForever(comicObs)
            verify(comicObs).onChanged(Resource.loading(null))
            viewModel.comics.removeObserver(comicObs)
        }
    }

    @Test
    fun getComics_Error() {
        testCoroutineRule.runBlockingTest{
            val errorMessage = "Your error message"

            doReturn(flow<ComicLocal> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(repository)
                .getComicsOfAHero(1)

            val viewModel = HeroListViewModel(marverRepository,repository)
            viewModel.getComics(1)
            viewModel.comics.observeForever(comicObs)
            verify(comicObs).onChanged(Resource.error(errorMessage, null))
            viewModel.comics.removeObserver(comicObs)
        }
    }


}