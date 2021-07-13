package com.carlyadam.github.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carlyadam.github.data.db.AppDatabase
import com.carlyadam.github.data.db.model.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// following given-when-then pattern

@RunWith(AndroidJUnit4::class)
class UserUnitTest {

    private var database: AppDatabase? = null

    @Before
    @Throws(Exception::class)
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @Test
    @Throws(InterruptedException::class)
    fun `testing inserting and showing user`() = runBlocking {

        // given
        val userTest: User = User(1, "carlyadam", "http://test.png", true, 5.0)

        val userList = ArrayList<User>()
        userList.add(userTest)

        // when
        database!!.userDao().insertAll(userList)

        val result = database!!.userDao().usersByName("carlyadam")

        // then
        assertEquals(userTest, result)
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database!!.close()
    }
}
