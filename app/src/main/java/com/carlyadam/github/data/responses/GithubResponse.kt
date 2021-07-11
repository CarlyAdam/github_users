package com.carlyadam.github.data.responses

import com.carlyadam.github.data.model.User

data class GithubResponse(
    val incomplete_results: Boolean,
    val items: List<User>,
    val total_count: Int
)