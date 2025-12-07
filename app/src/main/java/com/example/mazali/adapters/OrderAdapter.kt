package com.example.mazali.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mazali.Consts
import com.example.mazali.R
import com.example.mazali.data.FoodItem
import com.example.mazali.data.Order

class OrderAdapter(
    private val orderList: MutableList<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerLayout: LinearLayout = itemView.findViewById(R.id.headerLayout)
        val expandedLayout: LinearLayout = itemView.findViewById(R.id.expandedLayout)
        val textOrderDate: TextView = itemView.findViewById(R.id.textOrderDate)
        val textOrderTotal: TextView = itemView.findViewById(R.id.textOrderTotal)
        val textOrderStatus: TextView = itemView.findViewById(R.id.textOrderStatus)
        val imageExpand: ImageView = itemView.findViewById(R.id.imageExpand)
        val textOrderAddress: TextView = itemView.findViewById(R.id.textOrderAddress)
        val textOrderPayment: TextView = itemView.findViewById(R.id.textOrderPayment)
        val productsContainer: LinearLayout = itemView.findViewById(R.id.productsContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.textOrderDate.text = order.date
        holder.textOrderTotal.text = "${order.totalPrice} so'm"
        holder.textOrderStatus.text = order.status
        holder.textOrderAddress.text = "Manzil: ${order.address}"
        holder.textOrderPayment.text = "To‘lov: ${order.paymentMethod}"

        holder.imageExpand.rotation = if (order.isExpanded) 180f else 0f
        holder.expandedLayout.visibility = if (order.isExpanded) View.VISIBLE else View.GONE

        holder.headerLayout.setOnClickListener {
            order.isExpanded = !order.isExpanded
            notifyItemChanged(position)
        }

        holder.productsContainer.removeAllViews()
        for (product in order.products) {
            val itemView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.product_item_mini, holder.productsContainer, false)

            val imageProduct = itemView.findViewById<ImageView>(R.id.imageProductMini)
            val textName = itemView.findViewById<TextView>(R.id.textProductNameMini)
            val textQtyPrice = itemView.findViewById<TextView>(R.id.textProductQtyPrice)
            val textTotal = itemView.findViewById<TextView>(R.id.textProductTotal)

            textName.text = product.name
            textQtyPrice.text = "${product.quantity} x ${product.price}"
            val cleanPrice = product.price.filter { it.isDigit() }.toInt()
            textTotal.text = "${cleanPrice * product.quantity} so'm"

            Glide.with(holder.itemView.context)
                .load(Consts().BASE_URL+product.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageProduct)

            holder.productsContainer.addView(itemView)
        }
    }

    override fun getItemCount(): Int = orderList.size
}
