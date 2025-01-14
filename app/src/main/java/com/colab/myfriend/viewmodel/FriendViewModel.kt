package com.colab.myfriend.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.colab.myfriend.app.DataProduct
import com.colab.myfriend.repository.DataProductsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.crocodic.core.base.adapter.CorePagingSource
import com.crocodic.core.base.viewmodel.CoreViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val dataProductsRepo: DataProductsRepo,
) : CoreViewModel() {

    val queries = MutableStateFlow<Triple<String?, String?, String?>>(Triple(null, null, null))

    fun getPagingProducts() : Flow<PagingData<DataProduct>> {
        return queries.flatMapLatest {
            Pager(
                config = CorePagingSource.config(10),
                pagingSourceFactory = {
                    CorePagingSource(0) {page: Int, limit: Int ->
                        dataProductsRepo.pagingProducts(limit, page).first()
                    }
                }
            ).flow.cachedIn(viewModelScope)
        }
    }

    private val _product = MutableStateFlow<List<DataProduct>>(emptyList())
    val product: StateFlow<List<DataProduct>> = _product


    fun getProduct(keyword: String = "") = viewModelScope.launch {
        dataProductsRepo.getProducts(keyword).collect { it: List<DataProduct> ->
            _product.emit(it)
        }
    }

    fun sortProducts(sortBy: String = "", orderBy: String = "") = viewModelScope.launch {
        dataProductsRepo.sortProducts(sortBy, orderBy).collect {
            _product.emit(it)
        }
    }

    fun filterProducts(filter: String = "") = viewModelScope.launch {
        dataProductsRepo.filterProducts(filter).collect {
            _product.emit(it)
        }
    }


    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }

}