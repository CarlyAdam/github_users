package com.carlyadam.github.data.api.responses

import com.carlyadam.github.data.db.model.User

data class GithubResponse(
    val incomplete_results: Boolean,
    val items: List<User>,
    val total_count: Int
)
