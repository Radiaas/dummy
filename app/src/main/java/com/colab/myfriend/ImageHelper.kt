package com.colab.myfriend

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.example.myfriend.R

object ImageHelper {

    @JvmStatic
    @BindingAdapter("urlImage", "progressBar", requireAll = false)
    fun ImageView.loadUrlWithProgress(urlImage: String?, progressBar: ProgressBar?) {
        progressBar?.visibility = View.VISIBLE // Tampilkan ProgressBar

        Glide.with(context)
            .load(urlImage)
            .placeholder(R.drawable.person) // Placeholder sementara
            .error(R.drawable.ic_error) // Gambar error jika gagal
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE) // Tidak menggunakan disk cache
            .skipMemoryCache(true) // Lewati cache memori
            .listener(object : com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.visibility = View.GONE // Sembunyikan ProgressBar jika gagal
                    return false
                }

                override fun onResourceReady(
                    resource: android.graphics.drawable.Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.visibility = View.GONE // Sembunyikan ProgressBar jika berhasil
                    return false
                }
            })
            .into(this)
    }
}