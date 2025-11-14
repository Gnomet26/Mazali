package com.example.mazali.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.adapters.OrderAdapter
import com.example.mazali.data.Order
import com.example.mazali.utils.PrefsManager

class BuyuritmalarFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var ordersAdapter: OrderAdapter
    private lateinit var orderList: MutableList<Order>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buyuritmalar, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewOrders)
        emptyText = view.findViewById(R.id.textEmptyOrders)

        orderList = PrefsManager.getOrders(requireContext())

        ordersAdapter = OrderAdapter(orderList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ordersAdapter

        toggleEmptyView()

        return view
    }

    private fun toggleEmptyView() {
        if (orderList.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}
