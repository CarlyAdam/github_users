package com.carlyadam.github.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val login: String,
    val avatar: String,
    var favorite: Boolean,
    val score: Double
)
