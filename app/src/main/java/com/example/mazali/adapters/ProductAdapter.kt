package com.example.mazali.adapters

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mazali.Consts
import com.example.mazali.R

import com.example.mazali.data.FoodItem
import com.example.mazali.fragments.MahsulotFragment

class ProductAdapter(private var productList: MutableList<FoodItem>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageProduct)
        val nameText: TextView = itemView.findViewById(R.id.textProductName)
        val categoryText: TextView = itemView.findViewById(R.id.textProductCategory)
        val priceText: TextView = itemView.findViewById(R.id.textProductPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menyu_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.nameText.text = product.name
        holder.categoryText.text = product.category
        holder.priceText.text = "${product.price} $"
        Glide.with(holder.itemView.context)
            .load(Consts().BASE_URL + product.image)
            .into(holder.imageView)
        holder.itemView.setOnClickListener { view ->

            val bundle = Bundle().apply {
                putInt("foodID", product.id)
                putString("foodName", product.name)
                putString("foodPrice", product.price)
                putString("foodImage", Consts().BASE_URL + product.image)
                putString("foodCategory", product.category)
                putString("foodDescription", product.comment)

            }
            // 🔄 ProductFragmentga o‘tish
            val activity = view.context as FragmentActivity
            activity.supportFragmentManager.commit {
                replace(R.id.user_frame, MahsulotFragment::class.java, bundle)

            }
        }
    }

    // ------------------------------
    // Append qilish: yangi itemlarni qo'shish
    fun appendList(newItems: List<FoodItem>) {
        val startPos = productList.size
        productList.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }

    // Clear qilish: barcha itemlarni o'chirish
    fun clearList() {
        productList.clear()
        notifyDataSetChanged()
    }

    // Update qilish: mavjud funksiya
    fun updateList(newList: List<FoodItem>) {
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
    }
}