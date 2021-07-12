package com.carlyadam.github.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.IOException

suspend fun <T : Any> safeApiCall(
    call: suspend () -> Flow<Result<T>>,
    errorMessage: String
): Flow<Result<T>> =
    try {
        call.invoke()
    } catch (e: Exception) {
        flowOf(Result.Error(IOException(errorMessage, e)))
    }
