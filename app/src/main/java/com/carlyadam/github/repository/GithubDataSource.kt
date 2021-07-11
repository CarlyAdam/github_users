package com.carlyadam.github.repository

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.carlyadam.github.R
import com.carlyadam.github.data.api.ApiService
import com.carlyadam.github.data.api.ApiService.Companion.API_KEY
import com.carlyadam.github.data.model.User
import java.io.IOException

class GithubDataSource(
    private val apiService: ApiService,
    private val context: Context,
    private val query: String
) :
    PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1
        return try {
            val response = apiService.users(page, 25, query, API_KEY)

            val userList = response.body()!!.items

            Log.i("AAAA",userList.size.toString())

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