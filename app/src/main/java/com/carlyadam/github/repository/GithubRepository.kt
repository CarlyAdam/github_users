package com.carlyadam.github.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.carlyadam.github.data.api.ApiService
import com.carlyadam.github.data.db.dao.UserDao
import kotlinx.coroutines.flow.Flow
import com.carlyadam.github.data.api.model.User as ApiUser
import com.carlyadam.github.data.db.model.User as DbUser

class GithubRepository(
    private val apiService: ApiService,
    private val context: Context,
    private val userDao: UserDao
) {

    fun users(query: String): Flow<PagingData<ApiUser>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 2),
            pagingSourceFactory = {
                GithubDataSource(apiService, context, query, userDao)
            }
        ).flow
    }

    suspend fun setUserFavorite(user: DbUser, favorite: Boolean) {
        if (favorite) {
            userDao.insertUserFavorite(user)
        } else {
            userDao.deleteUserFavorite(user)
        }
    }
}
