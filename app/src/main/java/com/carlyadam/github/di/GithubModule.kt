package com.carlyadam.github.di

import android.content.Context
import com.carlyadam.github.data.api.ApiService
import com.carlyadam.github.data.db.dao.UserDao
import com.carlyadam.github.repository.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object GithubModule {

    @Provides
    @ActivityRetainedScoped
    fun githubRepository(
        apiService: ApiService,
        @ApplicationContext context: Context,
        userDao: UserDao
    ): GithubRepository {
        return GithubRepository(apiService, context, userDao)
    }
}
