package com.colab.myfriend.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.colab.myfriend.app.DataProduct
import com.colab.myfriend.repository.DataProductsRepo
import com.crocodic.core.base.adapter.CorePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.crocodic.core.base.viewmodel.CoreViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val dataProductsRepo: DataProductsRepo
) : CoreViewModel() {

    private val _product = MutableStateFlow<List<DataProduct>>(emptyList())

    private val queries = MutableStateFlow<Triple<String?, String?, String?>>(Triple(null, null, null))
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPagingProducts(): Flow<PagingData<DataProduct>> {
        return queries.flatMapLatest {
            Pager(
                config = CorePagingSource.config(10),
                pagingSourceFactory = {
                    CorePagingSource(0) { page: Int, limit: Int ->
                        dataProductsRepo.pagingProducts(limit, page).first()
                    }
                }
            ).flow
                .map { pagingData ->
                    pagingData.filterDistinct() // Filter data duplikat
                }
                .cachedIn(viewModelScope)
        }
    }

    private fun <T : Any> PagingData<T>.filterDistinct(): PagingData<T> {
        val seenIds = mutableSetOf<Any>()
        return this.filter { item ->
            val id = when (item) {
                is DataProduct -> item.id // Pastikan 'id' adalah field unik
                else -> null
            }
            if (id != null && seenIds.contains(id).not()) {
                seenIds.add(id)
                true
            } else {
                false
            }
        }
    }

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