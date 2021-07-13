package com.carlyadam.github.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.carlyadam.github.data.api.ApiService
import com.carlyadam.github.data.db.AppDatabase
import com.carlyadam.github.data.db.model.User
import kotlinx.coroutines.flow.Flow

class GithubRepository(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    private val context: Context
) {

    fun users(query: String): Flow<PagingData<User>> {
        val finalQuery = "%$query%"
        val pagingSourceFactory = { appDatabase.userDao().usersByName(finalQuery) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = GithubDataSource(
                query,
                apiService,
                appDatabase,
                context
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    suspend fun setUserFavorite(id: Long, favorite: Boolean) {
        appDatabase.userDao().setUserFavorite(id, favorite)
    }
}
