package com.colab.myfriend.Api

import com.colab.myfriend.database.User
import com.crocodic.core.api.ModelResponse
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("data")
    val user: User,
    @SerializedName("token")
    val token: String?
) : ModelResponse()