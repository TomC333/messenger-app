package com.sgvas21.ddadi21.messengerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentSearchObjectBinding

/**
 * RecyclerView adapter for displaying a list of User objects.
 */
class UserAdapter(
    private val onUserClick: (User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = FragmentSearchObjectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserViewHolder(private val binding: FragmentSearchObjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.username.text = user.username
            binding.role.text = user.profession

            Glide.with(binding.profileImage.context)
                .load(user.profileImageUrl)
                .placeholder(R.drawable.avatar_image_placeholder)
                .error(R.drawable.avatar_image_placeholder)
                .circleCrop()
                .into(binding.profileImage)

            binding.root.setOnClickListener {
                onUserClick(user)
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates.
     */
    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.username == newItem.username

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }
}
