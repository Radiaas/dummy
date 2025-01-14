package com.colab.myfriend.Api

import com.colab.myfriend.database.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): Response<User>
}