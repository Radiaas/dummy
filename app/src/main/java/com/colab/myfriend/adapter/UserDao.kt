package com.colab.myfriend.adapter

import androidx.room.Dao
import androidx.room.Query
import com.colab.myfriend.database.User
import com.crocodic.core.data.CoreDao

@Dao
interface UserDao : CoreDao<User> {

    @Query("SELECT * FROM User WHERE idDb = 1")
    suspend fun checkLogin(): User?

    @Query("DELETE FROM user")
    suspend fun deleteAll()

}