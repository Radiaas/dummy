package com.colab.myfriend.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.colab.myfriend.R
import com.colab.myfriend.adapter.FriendAdapter
import com.colab.myfriend.app.DataProduct
import com.colab.myfriend.btm_sht.BottomSheetFilterProducts
import com.colab.myfriend.btm_sht.BottomSheetSortingProducts
import com.colab.myfriend.databinding.ActivityItemFriendBinding
import com.colab.myfriend.databinding.ActivityMenuHomeBinding
import com.colab.myfriend.viewmodel.FriendViewModel
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.adapter.PaginationAdapter
import com.crocodic.core.base.adapter.ReactiveListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MenuHomeActivity : CoreActivity<ActivityMenuHomeBinding, FriendViewModel>(R.layout.activity_menu_home) {

    private lateinit var adapter: FriendAdapter
    private var productList = ArrayList<DataProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.recyclerView.adapter = adapterCore

        adapter = FriendAdapter(emptyList()) { _ ->
            // Tambahkan logika klik item di sini
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch {
            viewModel.getPagingProducts()
        }


        lifecycleScope.launch {
            viewModel.queries.emit(Triple("", "", ""))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getPagingProducts().collectLatest { data ->
                        adapterCore.submitData(data)
                    }
                }
            }
        }

        binding.searchBar.doOnTextChanged { text, start, before, count ->
            val keyword = "%${text.toString().trim()}%"
            Timber.d("Search keyword: $keyword")
            viewModel.getProduct(keyword)
        }

        binding.ftbnFilter.setOnClickListener {
            val btmSht = BottomSheetFilterProducts { filter ->
                viewModel.filterProducts(filter)
            }

            btmSht.show(supportFragmentManager, "BtmShtFilteringProducts")
        }

        binding.ftbnSort.setOnClickListener {
            val btmSht = BottomSheetSortingProducts { sortBy, order ->
                viewModel.sortProducts(sortBy, order)
            }

            btmSht.show(supportFragmentManager, "BtmShtSortingProducts")
        }

    }

    private val adapterCore by lazy {
        PaginationAdapter<ActivityItemFriendBinding, DataProduct>(R.layout.activity_item_friend)
    }


}
