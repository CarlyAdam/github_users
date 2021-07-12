package com.carlyadam.github.ui.github

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.carlyadam.github.data.db.model.User
import com.carlyadam.github.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
    private val state: SavedStateHandle
) :
    ViewModel() {

    fun users(query: String): Flow<PagingData<User>> {
        return githubRepository.users(query).cachedIn(viewModelScope)
    }

    fun setUserFavorite(id: Long, favorite: Boolean) {
        viewModelScope.launch {
            githubRepository.setUserFavorite(id, favorite)
        }
    }

    // handling process death
    fun saveQuery(query: String) {
        state.set("Query", query)
    }

    fun getQuery(): String? {
        return state.get<String>("Query")
    }
}
