package com.colab.myfriend.repository

import com.colab.myfriend.app.DataProduct
import kotlinx.coroutines.flow.Flow

interface DataProductsRepo {

    fun getProducts(keyword: String): Flow<List<DataProduct>>

    fun sortProducts(sortBy: String, order: String): Flow<List<DataProduct>>

    fun filterProducts(filter: String): Flow<List<DataProduct>>

    fun pagingProducts(limit: Int, skip: Int): Flow<List<DataProduct>>

}