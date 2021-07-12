package com.carlyadam.github.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlyadam.github.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
    private val state: SavedStateHandle
) :
    ViewModel() {

    fun setUserFavorite(id: Long, favorite: Boolean) {
        viewModelScope.launch {
            githubRepository.setUserFavorite(id, favorite)
        }
    }
}
