package com.colab.myfriend.Api

import com.colab.myfriend.LogoutResponse
import com.colab.myfriend.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiAuthService {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): UserResponse

    @GET("auth/logout")
    suspend fun logout(): LogoutResponse

}