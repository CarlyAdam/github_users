package com.carlyadam.github.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.carlyadam.github.data.api.ApiService
import com.carlyadam.github.data.model.User
import kotlinx.coroutines.flow.Flow

class GithubRepository(
    private val apiService: ApiService,
    private val context: Context
) {

    fun users(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 2),
            pagingSourceFactory = {
                GithubDataSource(apiService, context, query)
            }
        ).flow
    }
}
