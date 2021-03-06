package com.carlyadam.github.data.api

import com.carlyadam.github.BuildConfig
import com.carlyadam.github.data.api.responses.GithubResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val API_URL = "https://api.github.com/search/"
        const val API_KEY = BuildConfig.API_ACCESS_KEY
    }

    @GET("users")
    suspend fun users(
        @Query("page") offset: Int,
        @Query("per_page") limit: Int,
        @Query("q") query: String,
        @Query("access_token") token: String
    ): Response<GithubResponse>
}
