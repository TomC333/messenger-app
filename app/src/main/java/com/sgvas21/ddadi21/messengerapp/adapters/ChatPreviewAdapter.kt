package com.sgvas21.ddadi21.messengerapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.data.model.ChatPreview
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentSingleChatBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ChatPreviewAdapter(
    private val onChatClick: ((ChatPreview) -> Unit)? = null
) : RecyclerView.Adapter<ChatPreviewAdapter.ChatViewHolder>() {

    private val chats = mutableListOf<ChatPreview>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newChats: List<ChatPreview>) {
        chats.clear()
        chats.addAll(newChats)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = FragmentSingleChatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatViewHolder(binding)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    inner class ChatViewHolder(private val binding: FragmentSingleChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatPreview) {

            binding.tvName.text = chat.otherUserName
            binding.tvMessage.text = chat.chat.lastMessage
            binding.tvTime.text = formatTime(chat.chat.lastMessageTimestamp)

            Glide.with(binding.ivProfile.context)
                .load(chat.otherUserImageUrl)
                .placeholder(R.drawable.avatar_image_placeholder)
                .error(R.drawable.avatar_image_placeholder)
                .circleCrop()
                .into(binding.ivProfile)

            itemView.setOnClickListener {
                onChatClick?.invoke(chat)
            }
        }

        private fun formatTime(timestamp: Long): String {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }
    }
}