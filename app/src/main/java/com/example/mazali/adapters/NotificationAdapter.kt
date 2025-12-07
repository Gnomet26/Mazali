package com.example.mazali.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.data.NotificationItem


class NotificationAdapter(
    private val context: Context,
    private val notifications: MutableList<NotificationItem>,
    private val onMarkAsRead: (NotificationItem) -> Unit // Fragment orqali POST so‘rovni chaqirish uchun
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.textTitle)
        val tvMessage: TextView = itemView.findViewById(R.id.textBody)
        val cardView: CardView = itemView as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]

        holder.tvTitle.text = notification.title
        holder.tvMessage.text = notification.message

        // is_read bo‘yicha styling
        if (notification.is_read) {
            holder.tvTitle.setTypeface(null, Typeface.NORMAL)
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.gray_light))
        } else {
            holder.tvTitle.setTypeface(null, Typeface.BOLD)
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.white))
        }

        // Item click
        holder.itemView.setOnClickListener {
            // AlertDialog ko‘rsatish
            AlertDialog.Builder(context)
                .setTitle(notification.title)
                .setMessage(notification.message)
                .setPositiveButton("OK", null)
                .show()

            // Agar hali o‘qilmagan bo‘lsa, mark as read chaqirish
            if (!notification.is_read) {
                onMarkAsRead(notification)
                notification.is_read = true
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = notifications.size

    // RecyclerView yangilash uchun funksiya
    fun updateList(newList: List<NotificationItem>) {
        notifications.clear()
        notifications.addAll(newList)
        notifyDataSetChanged()
    }
}
