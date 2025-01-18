package com.colab.myfriend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myfriend.databinding.ItemLoadingBinding

class LoadingAdapter : LoadStateAdapter<LoadingAdapter.LoadingStateViewHolder>() {

    class LoadingStateViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        // Tidak ada binding data yang diperlukan karena hanya indikator loading
    }
}