package com.colab.myfriend.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phone: String?, // Diizinkan null
    val name: String?,
    val password: String?
)