package com.example.superherosquadmaker.ui.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.superherosquadmaker.data.api.APIClient

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class HeroListViewModelFactory(private val marverRepository: MarverRepository, private val localRepository: LocalDbRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HeroListViewModel::class.java)) {
            return HeroListViewModel(
                marverRepository, localRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}