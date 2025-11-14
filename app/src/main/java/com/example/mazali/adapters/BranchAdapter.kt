package com.example.mazali.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.data.Branch

class BranchAdapter(
    private val onClick: (Branch) -> Unit
) : ListAdapter<Branch, BranchAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Branch>() {
            override fun areItemsTheSame(oldItem: Branch, newItem: Branch): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Branch, newItem: Branch): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_branch, parent, false)
        return VH(v, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(itemView: View, private val onClick: (Branch) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val avatar: TextView = itemView.findViewById(R.id.tvAvatar)
        private val primary: TextView = itemView.findViewById(R.id.tvPrimary)
        private val secondary: TextView = itemView.findViewById(R.id.tvSecondary)
        private val chevron: ImageView = itemView.findViewById(R.id.ivChevron)

        fun bind(item: Branch) {
            // Primary — viloyat (bold)
            primary.text = item.region

            // Secondary — manzil (normal, kichik)
            secondary.text = item.address

            // Avatar — birinchi harf yoki qisqa kod
            avatar.text = primary.text.trim().first().toString()
            // Optionally customize avatar background color based on hash
            // itemView.findViewById<View>(R.id.tvAvatar).backgroundTintList = ...

            itemView.setOnClickListener { onClick(item) }
        }
    }
}
