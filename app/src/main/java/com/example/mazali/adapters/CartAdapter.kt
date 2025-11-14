package com.example.mazali.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.data.FoodItem

class CartAdapter(
    private val cartItems: MutableList<FoodItem>,
    private val listener: CartListener
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    interface CartListener {
        fun onQuantityChanged()
        fun onItemDeleted(position: Int)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageProduct)
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

        holder.image.setImageResource(item.imageResId)
        holder.name.text = item.name
        holder.category.text = item.category
        holder.price.text = item.price
        holder.qty.text = item.quantity.toString()

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
