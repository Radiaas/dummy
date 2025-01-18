package com.colab.myfriend

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.colab.myfriend.ImageHelper.loadUrlWithProgress
import com.colab.myfriend.app.DataProduct
import com.crocodic.core.base.activity.NoViewModelActivity
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityDetailProductBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailProductActivity :  NoViewModelActivity<ActivityDetailProductBinding>(R.layout.activity_detail_product)  {

    @Inject
    lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataIntent = intent.getStringExtra(DATA)
        binding.data = gson.fromJson(dataIntent, DataProduct::class.java)

        val imageUrl = intent.getStringExtra(DATA) // Ambil URL gambar dari Intent
        binding.ivPhoto.loadUrlWithProgress(imageUrl, binding.progressBar)
    }
    companion object {
        const val DATA = "data"
    }
}