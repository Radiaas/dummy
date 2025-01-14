package com.colab.myfriend.app

import com.google.gson.annotations.SerializedName

data class DataProduct(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("images")
    val images: List<String>
)
