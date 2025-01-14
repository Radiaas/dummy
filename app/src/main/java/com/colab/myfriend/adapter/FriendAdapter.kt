package com.colab.myfriend.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.colab.myfriend.app.DataProduct
import com.colab.myfriend.R

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

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newProducts: List<DataProduct>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = productList.size
            override fun getNewListSize(): Int = newProducts.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return productList[oldItemPosition].id == newProducts[newItemPosition].id
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return productList[oldItemPosition] == newProducts[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        productList = newProducts.toList()
        diffResult.dispatchUpdatesTo(this) // Jangan gunakan notifyDataSetChanged
    }


    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_friend_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tv_friend_school)
        private val profileImageView: ImageView = itemView.findViewById(R.id.img_friend)

        fun bind(product: DataProduct, onItemClick: (DataProduct) -> Unit) {
            // Bind text data
            titleTextView.text = product.title
            descriptionTextView.text = product.description

            // Load the first image URL or set a placeholder if the list is empty
            if (product.images.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(product.images[0]) // Load the first image
                    .placeholder(R.drawable.ic_profile_placeholder) // Placeholder image
                    .into(profileImageView)
            } else {
                // Set a placeholder for items with no image
                profileImageView.setImageResource(R.drawable.ic_profile_placeholder)
            }

            // Handle item click
            itemView.setOnClickListener {
                onItemClick(product)
            }
        }
    }

}