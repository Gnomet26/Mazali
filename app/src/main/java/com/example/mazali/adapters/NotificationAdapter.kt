package com.example.mazali.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.data.NotificationItem

class NotificationAdapter(
    private val notifications: List<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.imageIcon)
        val title: TextView = itemView.findViewById(R.id.textTitle)
        val body: TextView = itemView.findViewById(R.id.textBody)
        val time: TextView = itemView.findViewById(R.id.textTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.icon.setImageResource(notification.iconResId)
        holder.title.text = notification.title
        holder.body.text = notification.message
        holder.time.text = notification.time
    }

    override fun getItemCount(): Int = notifications.size
}