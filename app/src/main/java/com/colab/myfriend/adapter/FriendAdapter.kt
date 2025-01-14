package com.colab.myfriend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.colab.myfriend.app.DataProduct
import com.example.myfriend.R

class FriendAdapter(
    private var productList: List<DataProduct>,
    private val onItemClick: (DataProduct) -> Unit
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product, onItemClick)
    }

    override fun getItemCount(): Int = productList.size

    fun updateData(newProducts: List<DataProduct>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = productList.size
            override fun getNewListSize(): Int = newProducts.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                // Membandingkan apakah item adalah sama berdasarkan ID
                return productList[oldItemPosition].id == newProducts[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                // Membandingkan apakah isi item sama persis
                return productList[oldItemPosition] == newProducts[newItemPosition]
            }
        }

        // Hitung perbedaan data menggunakan DiffUtil
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        productList = newProducts // Perbarui data dengan list baru
        diffResult.dispatchUpdatesTo(this) // Terapkan perubahan ke RecyclerView
    }

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_friend_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_friend_school)
        private val profileImageView: ImageView = itemView.findViewById(R.id.img_friend)

        fun bind(product: DataProduct, onItemClick: (DataProduct) -> Unit) {
            titleTextView.text = product.title
            descriptionTextView.text = product.description

            // Placeholder image handling, or set a default image for all products
            profileImageView.setImageResource(R.drawable.ic_profile_placeholder)

            itemView.setOnClickListener {
                onItemClick(product)
            }
        }
    }
}