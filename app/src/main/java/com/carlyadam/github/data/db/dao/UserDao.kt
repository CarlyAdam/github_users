package com.carlyadam.github.data.db.dao

import androidx.room.*
import com.carlyadam.github.data.db.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(persons: List<User>)

    @Query("SELECT * FROM user_table WHERE favorite= :favorite")
    suspend fun getUsersFavorite(favorite: Boolean): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFavorite(user: User)

    @Delete
    suspend fun deleteUserFavorite(user: User)
}
