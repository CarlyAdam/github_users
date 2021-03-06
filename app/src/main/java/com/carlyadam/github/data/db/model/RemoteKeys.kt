package com.carlyadam.github.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val userId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
