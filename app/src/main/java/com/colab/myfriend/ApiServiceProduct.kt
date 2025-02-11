package com.colab.myfriend

import com.colab.myfriend.Api.ResponseDataProduct
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceProduct {

    @GET("products/search")
    suspend fun getProducts(
        @Query("q") keyword: String
    ) : ResponseDataProduct

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
    suspend fun pagingProduct(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): ResponseDataProduct

}