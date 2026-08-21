package com.example.aspro.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.aspro.R

class ChatAdapter(private val chatMessages: MutableList<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Model Message Views
        val messageTextViewModel: TextView = view.findViewById(R.id.messageTextViewModel)
        val messageImageViewModel: ImageView = view.findViewById(R.id.imageViewModel)
        val vectorImageViewModel: ImageView = view.findViewById(R.id.vectorImageViewModel)

        // User Message Views
        val messageTextViewUser: TextView = view.findViewById(R.id.messageTextViewUser)
        val messageImageViewUser: ImageView = view.findViewById(R.id.imageViewUser)
        val vectorImageViewUser: ImageView = view.findViewById(R.id.vectorImageViewUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = chatMessages[position]

        if (chatMessage.isUser) {
            // Handle user message views
            holder.vectorImageViewUser.visibility = View.VISIBLE
            holder.messageTextViewUser.visibility = View.VISIBLE

            if (chatMessage.message.isNotBlank()) {
                holder.messageTextViewUser.text = chatMessage.message
                holder.messageTextViewUser.visibility = View.VISIBLE
            } else {
                holder.messageTextViewUser.visibility = View.GONE
            }

            if (chatMessage.bitmap != null) {
                holder.messageImageViewUser.setImageBitmap(chatMessage.bitmap)
                holder.messageImageViewUser.visibility = View.VISIBLE
            } else {
                holder.messageImageViewUser.visibility = View.GONE
            }


            holder.vectorImageViewModel.visibility = View.GONE
            holder.messageTextViewModel.visibility = View.GONE
            holder.messageImageViewModel.visibility = View.GONE

            val layoutParams = holder.messageTextViewUser.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.startToStart = ConstraintLayout.LayoutParams.UNSET
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            holder.messageTextViewUser.layoutParams = layoutParams

            val imageLayoutParams = holder.messageImageViewUser.layoutParams as ConstraintLayout.LayoutParams
            imageLayoutParams.startToStart = ConstraintLayout.LayoutParams.UNSET
            imageLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            holder.messageImageViewUser.layoutParams = imageLayoutParams

        } else {
            // Handle model message views
            holder.vectorImageViewModel.visibility = View.VISIBLE
            holder.messageTextViewModel.visibility = View.VISIBLE

            if (chatMessage.message.isNotBlank()) {
                holder.messageTextViewModel.text = chatMessage.message
                holder.messageTextViewModel.visibility = View.VISIBLE
            } else {
                holder.messageTextViewModel.visibility = View.GONE
            }

            if (chatMessage.bitmap != null) {
                holder.messageImageViewModel.setImageBitmap(chatMessage.bitmap)
                holder.messageImageViewModel.visibility = View.VISIBLE
            } else {
                holder.messageImageViewModel.visibility = View.GONE
            }

            holder.vectorImageViewUser.visibility = View.GONE
            holder.messageTextViewUser.visibility = View.GONE
            holder.messageImageViewUser.visibility = View.GONE

            val layoutParams = holder.messageTextViewModel.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.startToEnd = R.id.vectorImageViewModel
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            holder.messageTextViewModel.layoutParams = layoutParams

            val imageLayoutParams = holder.messageImageViewModel.layoutParams as ConstraintLayout.LayoutParams
            imageLayoutParams.startToEnd = R.id.vectorImageViewModel
            imageLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            holder.messageImageViewModel.layoutParams = imageLayoutParams
        }
    }

    override fun getItemCount() = chatMessages.size

    fun addMessage(message: ChatMessage) {
        chatMessages.add(message)
        notifyItemInserted(chatMessages.size - 1)
    }
}
