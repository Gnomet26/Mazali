package com.example.mazali.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mazali.Consts
import com.example.mazali.R
import com.example.mazali.data.FoodItem
import kotlin.contracts.contract
import kotlin.coroutines.coroutineContext
import kotlin.math.log

private lateinit var imageResource: String

class CartAdapter(
    private val cartItems: MutableList<FoodItem>,
    private val listener: CartListener
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    interface CartListener {
        fun onQuantityChanged()
        fun onItemDeleted(position: Int)
    }
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product_image: ImageView = itemView.findViewById(R.id.imageProduct1)
        val name: TextView = itemView.findViewById(R.id.textProductName)
        val category: TextView = itemView.findViewById(R.id.textCategory)
        val price: TextView = itemView.findViewById(R.id.textPrice)
        val minus: TextView = itemView.findViewById(R.id.buttonMinus)
        val plus: TextView = itemView.findViewById(R.id.buttonPlus)
        val qty: TextView = itemView.findViewById(R.id.textQuantity)
        val deleteBtn: ImageView = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.product_image)
        holder.name.text = item.name
        holder.category.text = item.category
        holder.price.text = item.price
        holder.qty.text = item.quantity.toString()

        Log.d("TTEST",Consts().BASE_URL + item.image)
        // ➖ Miqdorni kamaytirish
        holder.minus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                holder.qty.text = item.quantity.toString()
                listener.onQuantityChanged()
            } else {
                // agar 1 bo‘lsa va yana minus bosilsa — o‘chiramiz
                cartItems.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartItems.size)
                listener.onItemDeleted(position)
            }
        }

        // ➕ Miqdorni oshirish
        holder.plus.setOnClickListener {
            item.quantity++
            holder.qty.text = item.quantity.toString()
            listener.onQuantityChanged()
        }

        // 🗑️ O‘chirish tugmasi
        holder.deleteBtn.setOnClickListener {
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
            listener.onItemDeleted(position)
        }
    }
    override fun getItemCount(): Int = cartItems.size
}
