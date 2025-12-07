package com.example.mazali.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.data.model.Order
import kotlin.let

class BuyurtmaAdapter(
    private val orders: MutableList<Order>
) : RecyclerView.Adapter<BuyurtmaAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textOrderId: TextView = view.findViewById(R.id.textOrderId)
        val textOrderStatus: TextView = view.findViewById(R.id.textOrderStatus)
        val textOrderDate: TextView = view.findViewById(R.id.textOrderDate)
        val textOrderLocation: TextView = view.findViewById(R.id.textOrderLocation)
        val nestedRecyclerView: RecyclerView = view.findViewById(R.id.recyclerViewItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.textOrderId.text = "Order #${order.id}"
        holder.textOrderStatus.text = order.status
        holder.textOrderDate.text = order.createdAt
        holder.textOrderLocation.text = "Lat: ${order.lat}, Long: ${order.long}"

        // Nested RecyclerView uchun adapter
        val itemAdapter = order.products?.let { ItemAdapter(it) } ?: ItemAdapter(emptyList())
        holder.nestedRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.nestedRecyclerView.adapter = itemAdapter

        // Expand / Collapse holatini saqlash
        holder.nestedRecyclerView.visibility = if (order.isExpanded) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            order.isExpanded = !order.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = orders.size

    // Adapterdagi ma'lumotlarni yangilash
    fun updateOrders(newOrders: List<Order>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }
}
