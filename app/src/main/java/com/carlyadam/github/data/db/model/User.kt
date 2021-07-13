package com.carlyadam.github.data.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Long,
    val login: String,
    @field:SerializedName("avatar_url") val avatar: String,
    var favorite: Boolean,
    val score: Double
)

