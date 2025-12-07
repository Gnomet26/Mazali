package com.example.mazali.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mazali.R
import com.example.mazali.data.model.ProductItem
import kotlin.collections.getOrNull

class ItemAdapter(
    private val items: List<ProductItem>?
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProduct: ImageView = view.findViewById(R.id.imageProduct)
        val textProductName: TextView = view.findViewById(R.id.textProductName)
        val textProductPrice: TextView = view.findViewById(R.id.textProductPrice)
        val textProductCount: TextView = view.findViewById(R.id.textProductCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items?.getOrNull(position)
        if (item == null) {
            Toast.makeText(holder.itemView.context, "Mahsulot ma'lumotlari yo‘q", Toast.LENGTH_SHORT).show()
            return
        }

        holder.textProductName.text = item.name ?: "No name"
        holder.textProductPrice.text = "${item.price ?: "0"} so'm"
        holder.textProductCount.text = "Count: ${item.count ?: 0}"

        Glide.with(holder.itemView.context)
            .load(item.image ?: "")
            .into(holder.imageProduct)
    }

    override fun getItemCount(): Int = items?.size ?: 0
}
