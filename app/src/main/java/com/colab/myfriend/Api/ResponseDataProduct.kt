package com.colab.myfriend.Api

import com.colab.myfriend.app.DataProduct
import com.crocodic.core.api.ModelResponse
import com.google.gson.annotations.SerializedName

data class ResponseDataProduct(
    @SerializedName("products")
    val products: List<DataProduct>
) : ModelResponse()