package com.example.superherosquadmaker.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superherosquadmaker.data.localdb.ComicLocal
import com.example.superherosquadmaker.data.localdb.Hero
import com.example.superherosquadmaker.utils.fromHeroToSuperHero
import com.example.superherosquadmaker.utils.Resource
import com.example.superherosquadmaker.utils.fromMarvelComicToLocalComic
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class HeroListViewModel(
    val marverRepository: MarverRepository,
    private val localRepository: LocalDbRepository
) : ViewModel() {
    private val REQUESTED_CHARACTERS = 20
    private var offset = 0

    private val _heroes = MutableLiveData<Resource<List<Hero>>>()
    val heroes: LiveData<Resource<List<Hero>>> = _heroes

    private val _mySquad = MutableLiveData<Resource<List<Hero>>>()
    val mySquad: LiveData<Resource<List<Hero>>> = _mySquad

    private val _comics = MutableLiveData<Resource<List<ComicLocal>>>()
    val comics: LiveData<Resource<List<ComicLocal>>> = _comics

    private val _loadingMore = MutableLiveData<Resource<Boolean>>()
    val loadingMore: LiveData<Resource<Boolean>> = _loadingMore

    init {
        getMySquad()
    }

    private fun getMySquad() {
        viewModelScope.launch {
            localRepository.getMySquad()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _mySquad.postValue(Resource.error(e.message.toString(), null))
                }
                .collect {
                    _mySquad.postValue(Resource.success(it))
                }
        }
    }

    @ExperimentalCoroutinesApi
    fun updateCharacter(hero: Hero) {
        viewModelScope.launch {
            localRepository.removeOrAddToMySquad(hero)
                .flowOn(Dispatchers.Default)
                .catch { e ->
                    _heroes.postValue(Resource.error(e.toString(), null))
                }.collect {
                    getMySquad()
                    _heroes.postValue(Resource.success(listOf(hero)))
                }
        }
    }

    @ExperimentalCoroutinesApi
    fun getHeroes() {
        viewModelScope.launch {
            _heroes.postValue(Resource.loading(null))
            localRepository.getAllHeroes()
                .flatMapConcat { usersStored ->
                    if (usersStored.isEmpty()) {
                        return@flatMapConcat marverRepository.getAllHeroes(
                            REQUESTED_CHARACTERS
                        ).map { heroesData ->
                            if (heroesData != null) {
                                offset = REQUESTED_CHARACTERS
                            }
                            heroesData?.results?.map { it.fromHeroToSuperHero() }?.toList()
                        }.flatMapConcat { heroes ->
                            localRepository.insertAll(heroes!!).flatMapConcat {
                                flow {
                                    emit(heroes)
                                }
                            }
                        }
                    } else {
                        return@flatMapConcat flow {
                            offset = usersStored.size
                            emit(usersStored)
                        }
                    }
                }
                .flowOn(Dispatchers.Default)
                .catch { e ->
                    _heroes.postValue(Resource.error(e.message.toString(), null))
                }
                .collect {
                    _heroes.postValue(Resource.success(it))
                }
        }
    }

    @ExperimentalCoroutinesApi
    fun getMoreHeroes() {
        viewModelScope.launch {
            _loadingMore.postValue(Resource.loading(true))
            marverRepository.getMoreHeroes(
                REQUESTED_CHARACTERS,
                offset
            ).map { heroesData ->
                offset += REQUESTED_CHARACTERS
                heroesData?.results?.map { it.fromHeroToSuperHero() }?.toList()

            }.flatMapConcat { newHeroes ->
                return@flatMapConcat localRepository.insertAll(newHeroes!!).flatMapConcat {
                    flow {
                        emit(heroes)
                    }
                }
            }.flowOn(Dispatchers.Default)
                .catch { e ->
                    _loadingMore.postValue(Resource.error(e.message.toString(), null))
                }
                .collect {
                    localRepository.getAllHeroes().flowOn(Dispatchers.Default).collect {
                        _loadingMore.postValue(Resource.loading(false))
                        _heroes.postValue(Resource.success(it))
                    }
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
                        ).map { comicsData ->
                            comicsData!!.results?.map { it.fromMarvelComicToLocalComic(characterId) }
                                ?.toList()
                        }.flatMapConcat { comics ->
                            localRepository.insertAllComics(comics!!).flatMapConcat {
                                flow {
                                    emit(comics)
                                }
                            }
                        }
                    } else {
                        flow {
                            emit(storedComics)
                        }
                    }
                }.flowOn(Dispatchers.Default)
                .catch { e ->
                    _comics.postValue(Resource.error(e.message.toString(), null))
                }.collect {
                    _comics.postValue(Resource.success(it))
                }
        }
    }

    @ExperimentalCoroutinesApi
    fun getHeroByName(name: String) {
        viewModelScope.launch {
            localRepository.getHeroByName(name)
                .flatMapConcat { hero ->
                    if (hero != null) {
                        flow {
                            emit { listOf(hero) }
                        }
                    } else {
                        return@flatMapConcat marverRepository.getHeroByName(name).map { it ->
                            it.results?.map { it.fromHeroToSuperHero() }?.toList()
                        }.flatMapConcat { onlineHeroes ->
                            if (onlineHeroes != null) {
                                localRepository.insertAll(onlineHeroes).flatMapConcat { _ ->
                                    flow {
                                        emit { onlineHeroes }
                                    }
                                }
                            } else {
                                flow { emit { null } }
                            }
                        }
                    }
                }.flowOn(Dispatchers.Default)
                .catch { e ->

                }.collect {
                    _heroes.postValue(Resource.success(it.invoke()))

                }
        }
    }

}

