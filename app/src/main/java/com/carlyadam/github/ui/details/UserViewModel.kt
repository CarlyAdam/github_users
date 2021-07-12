package com.carlyadam.github.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlyadam.github.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.carlyadam.github.data.db.model.User as DbUser

@HiltViewModel
class UserViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
    private val state: SavedStateHandle
) :
    ViewModel() {

    fun setUserFavorite(user: DbUser, favorite: Boolean) {
        viewModelScope.launch {
            githubRepository.setUserFavorite(user, favorite)
        }
    }
}
