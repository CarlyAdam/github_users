package com.carlyadam.github.data.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val login: String,
    val avatar: String,
    var favorite: Boolean,
    val score: Double
) : Parcelable

