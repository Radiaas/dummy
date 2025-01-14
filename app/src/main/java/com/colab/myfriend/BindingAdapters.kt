package com.colab.myfriend

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder) // Placeholder image
                        .error(R.drawable.ic_profile_placeholder)       // Error image
                )
                .into(view)
        } else {
            view.setImageResource(R.drawable.ic_profile_placeholder)
        }
    }
}
