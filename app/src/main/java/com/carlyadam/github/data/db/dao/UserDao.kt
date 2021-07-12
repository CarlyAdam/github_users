package com.carlyadam.github.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carlyadam.github.data.db.model.User

@Dao
interface UserDao {

    @Query("UPDATE users SET favorite =:favorite WHERE id =:id ")
    suspend fun setUserFavorite(id: Long, favorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Query("SELECT * FROM users WHERE login LIKE :queryString")
    fun usersByName(queryString: String): PagingSource<Int, User>

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}
