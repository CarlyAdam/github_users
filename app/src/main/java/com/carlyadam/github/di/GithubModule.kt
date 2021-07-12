package com.carlyadam.github.di

import android.content.Context
import com.carlyadam.github.data.api.ApiService
import com.carlyadam.github.data.db.AppDatabase
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
        appDatabase: AppDatabase
    ): GithubRepository {
        return GithubRepository(apiService, appDatabase)
    }
}
