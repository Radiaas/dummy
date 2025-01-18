package com.colab.myfriend.activity

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.colab.myfriend.DetailProductActivity
import com.colab.myfriend.LoadingAdapter
import com.colab.myfriend.app.DataProduct
import com.colab.myfriend.btm_sht.BottomSheetFilterProducts
import com.colab.myfriend.btm_sht.BottomSheetSortingProducts
import com.colab.myfriend.viewmodel.FriendViewModel
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.adapter.PaginationAdapter
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.toJson
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityItemFriendBinding
import com.example.myfriend.databinding.ActivityMenuHomeBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MenuHomeActivity :  CoreActivity<ActivityMenuHomeBinding, FriendViewModel>(R.layout.activity_menu_home) {

    @Inject
    lateinit var gson: Gson

    private val adapterCore by lazy {
        PaginationAdapter<ActivityItemFriendBinding, DataProduct>(R.layout.activity_item_friend).initItem { _, data ->
            openActivity<DetailProductActivity> {
                val dataProduct = data.toJson(gson)
                putExtra(DetailProductActivity.DATA, dataProduct)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        val adapterWithFooter = adapterCore.withLoadStateFooter(
            footer = LoadingAdapter()
        )
        binding.recyclerView.adapter = adapterWithFooter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.slider.collect { data ->
                        binding.ivSlider.setImageList(data)
                    }
                }

                launch {
                    viewModel.getPagingProducts().collectLatest { data ->
                        adapterCore.submitData(data)
                    }
                }
            }
        }


        viewModel.getSlider()

        binding.searchBar.doOnTextChanged { text, _, _, _ ->
            val keyword = "%${text.toString().trim()}%"
            viewModel.getProduct(keyword)
        }

        binding.btnFilter.setOnClickListener {
            val btmSht = BottomSheetFilterProducts { filter ->
                viewModel.filterProducts(filter)
            }
            btmSht.show(supportFragmentManager, "BtmShtFilteringProducts")
        }

        binding.btnSort.setOnClickListener {
            val btmSht = BottomSheetSortingProducts { sortBy, order ->
                viewModel.sortProducts(sortBy, order)
            }
            btmSht.show(supportFragmentManager, "BtmShtSortingProducts")
        }
    }


}