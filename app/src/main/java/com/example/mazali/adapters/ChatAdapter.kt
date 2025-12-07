package com.example.mazali.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.data.model.ChatMessage

class ChatAdapter(
    private val context: Context,
    private val messages: MutableList<ChatMessage>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.text

        val params = holder.messageTextView.layoutParams as LayoutParams

        if (message.isBot) {
            holder.messageTextView.background = ContextCompat.getDrawable(context, R.drawable.rounded_message_bot_bg)
            params.startToStart = LayoutParams.PARENT_ID
            params.endToEnd = LayoutParams.UNSET
        } else {
            holder.messageTextView.background = ContextCompat.getDrawable(context, R.drawable.rounded_message_user_bg)
            params.endToEnd = LayoutParams.PARENT_ID
            params.startToStart = LayoutParams.UNSET
        }

        holder.messageTextView.layoutParams = params

        // Max width: ekran kengligining 70%
        val displayMetrics = context.resources.displayMetrics
        holder.messageTextView.maxWidth = (displayMetrics.widthPixels * 0.7).toInt()
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
