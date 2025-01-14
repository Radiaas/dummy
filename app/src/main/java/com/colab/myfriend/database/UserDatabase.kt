package com.colab.myfriend.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.colab.myfriend.adapter.UserDao

@Database(entities = [User::class], version = 2, exportSchema = false) // Versi diubah menjadi 2
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration() // Menghapus database lama jika skema berubah
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}