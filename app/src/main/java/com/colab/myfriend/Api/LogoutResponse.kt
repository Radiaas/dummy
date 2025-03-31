package com.colab.myfriend.Api

import com.crocodic.core.api.ModelResponse
import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("success")
    val isLogout: Boolean
) : ModelResponse()