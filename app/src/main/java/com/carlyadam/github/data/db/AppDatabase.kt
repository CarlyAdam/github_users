package com.carlyadam.github.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carlyadam.github.data.db.dao.UserDao
import com.carlyadam.github.data.db.model.User
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * The Room database for this app
 */

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        //encripting database
        var testBytes = charArrayOf('t', 'e', 's', 't')
        val passphrase = SQLiteDatabase.getBytes(testBytes)
        val factory = SupportFactory(passphrase)


        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "GITHUB"
            )
                .openHelperFactory(factory)
                .build()
    }
}
