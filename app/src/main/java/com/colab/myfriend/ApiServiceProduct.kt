package com.colab.myfriend

import com.colab.myfriend.Api.ResponseDataProduct
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceProduct {

    @GET("products/search")
    suspend fun getProduct(
        @Query("q") keyword: String
    ): ResponseDataProduct

    @GET("products/search")
    suspend fun sortProducts(
        @Query("sortBy") sortBy: String,
        @Query("order") order: String
    ): ResponseDataProduct

    @GET("products/category/{category}")
    suspend fun filterProducts(
        @Path("category") category: String
    ): ResponseDataProduct

    @GET("products")
    suspend fun pagingProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ResponseDataProduct

    @GET("products?limit=10&skip=90&select=thumbnail,title")
    suspend fun getSlider(): ResponseDataProduct
}