package com.carlyadam.github.ui.github.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.carlyadam.github.data.db.model.User
import com.carlyadam.github.databinding.ItemUserBinding
import com.carlyadam.github.utils.loadImage

class GithubAdapter(
    private val listener: AdapterListener,
    private val context: Context
) :
    PagingDataAdapter<User, GithubAdapter.ViewHolder>(
        DiffCallback
    ) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.apply {
            holder.bind(data!!)

            holder.binding.checkBox.apply {
                this.setOnClickListener {
                    listener.onFavoriteTap(
                        data,
                        this.isChecked
                    )
                }
            }
        }
    }

    inner class ViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemTap(item)
                    }
                }
            }
        }

        fun bind(user: User) {
            binding.apply {
                imageViewUser.loadImage(context, user.avatar, progressBar)
                textViewUserName.text = user.login
                checkBox.isChecked = user.favorite
            }
        }
    }

    interface AdapterListener {
        fun onItemTap(user: User)
        fun onFavoriteTap(user: User, favorite: Boolean)
    }
}
