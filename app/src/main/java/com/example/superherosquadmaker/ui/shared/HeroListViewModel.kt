package com.example.superherosquadmaker.ui.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.data.model.heroesCollection.HeroesData
import com.example.superherosquadmaker.utils.fromHeroToSuperHero
import com.example.superherosquadmaker.utils.Resource
import com.example.superherosquadmaker.utils.fromMarvelComicToLocalComic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HeroListViewModel(
    val marverRepository: MarverRepository,
    private val localRepository: LocalDbRepository
) : ViewModel() {

    private val _heroes = MutableLiveData<Resource<List<Hero>>>()
    val heroes: LiveData<Resource<List<Hero>>> = _heroes

    private val _mySquad = MutableLiveData<Resource<List<Hero>>>()
    val mySquad: LiveData<Resource<List<Hero>>> = _mySquad

    private val _comics = MutableLiveData<Resource<List<ComicLocal>>>()
    val comics: LiveData<Resource<List<ComicLocal>>> = _comics

    private val _heroesCount = MutableLiveData<Int>()
    val heroesCount: LiveData<Int> = _heroesCount

    private val _heroesOffset = MutableLiveData<Int>()
    val heroesOffset: LiveData<Int> = _heroesOffset

    init {
        getMySquad()
    }

     fun getMySquad() {
        viewModelScope.launch {
            localRepository.getMySquad()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _mySquad.postValue(Resource.error(e.toString(), null))
                }
                .collect {
                    _mySquad.postValue(Resource.success(it))
                }
        }
    }

    fun updateCharacter(hero: Hero) {
        viewModelScope.launch{
            localRepository.removeOrAddToMySquad(hero)
                .flowOn(Dispatchers.Default)
                .catch { e ->
                   _heroes.postValue(Resource.error(e.toString(), null))
               }.collect{
                    getMySquad()
                    _heroes.postValue(Resource.success(listOf(hero)))
                }
        }
    }

    fun getHeroes() {
        viewModelScope.launch {
            _heroes.postValue(Resource.loading(null))
            localRepository.getAllHeroes()
                .flatMapConcat { usersStored ->
                    if (usersStored.isEmpty()) {
                        return@flatMapConcat marverRepository.getAllHeroes(
                            20
                        ).map { heroesData ->
                            normalizeHeroResults(heroesData!!)
                        }.flatMapConcat { heroes ->
                            localRepository.insertAll(heroes!!).flatMapConcat {
                                flow {
                                    emit(heroes)
                                }
                            }
                        }
                    } else {
                        return@flatMapConcat flow {
                            emit(usersStored)
                        }
                    }
                }
                .flowOn(Dispatchers.Default)
                .catch { e ->
                    _heroes.postValue(Resource.error(e.toString(), null))
                }
                .collect {
                    _heroes.postValue(Resource.success(it))
                }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getComics(characterId: Int) {
        viewModelScope.launch {
            _comics.postValue(Resource.loading(null))
            localRepository.getComicsOfAHero(characterId)
                .flatMapConcat { storedComics ->
                    if (storedComics.isEmpty()) {
                        return@flatMapConcat marverRepository.getAllComics(
                            characterId
                        ).map{comicsData ->
                            comicsData!!.results?.map { it.fromMarvelComicToLocalComic(characterId) }?.toList()
                        }.flatMapConcat { comics ->
                            Log.i("comics", comics?.size.toString())
                                localRepository.insertAllComics(comics!!).flatMapConcat {
                                    flow {
                                        emit(comics)
                                    }
                                }
                        }
                    } else {
                        return@flatMapConcat flow {
                            emit { storedComics }
                        }
                    }
                    }.flowOn(Dispatchers.Default)
                     .catch { e ->
                            _comics.postValue(Resource.error(e.toString(), null))
                     }.collect {
                            _comics.postValue(Resource.success(it) as Resource<List<ComicLocal>>?)
                     }
        }
    }

    private fun normalizeHeroResults(data: HeroesData): List<Hero>? {
        _heroesCount.postValue(
            data.count
        )
        _heroesOffset.postValue(
            data.offset
        )
        return data.results?.map { it.fromHeroToSuperHero() }?.toList()
    }

}