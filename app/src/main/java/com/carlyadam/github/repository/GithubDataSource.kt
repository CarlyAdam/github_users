package com.carlyadam.github.repository

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.carlyadam.github.R
import com.carlyadam.github.data.api.ApiService
import com.carlyadam.github.data.api.ApiService.Companion.API_KEY
import com.carlyadam.github.data.api.model.User
import com.carlyadam.github.data.db.dao.UserDao
import java.io.IOException

class GithubDataSource(
    private val apiService: ApiService,
    private val context: Context,
    private val query: String,
    private val userDao: UserDao
) :
    PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1
        return try {
            val response = apiService.users(page, 25, query, API_KEY)

            val userList = response.body()!!.items
            val userLocals = userDao.getUsersFavorite(true)
            for (i in 0 until userList.size) {
                for (j in 0 until userLocals.size) {
                    if (userList[i].id == userLocals[j].id) {
                        userList[i].favorite = userLocals[j].favorite
                    }
                }
            }

            LoadResult.Page(
                data = userList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.body()!!.items.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            val error = IOException(context.getString(R.string.no_connection))
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition
    }
}
