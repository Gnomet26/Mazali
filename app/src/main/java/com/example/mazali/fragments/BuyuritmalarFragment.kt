package com.example.mazali.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mazali.R
import com.example.mazali.adapters.BuyurtmaAdapter
import com.example.mazali.data.repository.BuyurtmaRepository
import com.example.mazali.ui.auth.viewmodel.BuyurtmaViewModel
import com.example.mazali.ui.auth.viewmodel.BuyurtmaViewModelFactory
import com.example.mazali.utils.PrefsManager

class BuyuritmalarFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BuyurtmaAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var viewModel: BuyurtmaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_buyuritmalar, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewOrders)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyurtmaAdapter(mutableListOf())
        recyclerView.adapter = adapter

        val repository = BuyurtmaRepository()
        val factory = BuyurtmaViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BuyurtmaViewModel::class.java]

        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            if (!orders.isNullOrEmpty()) adapter.updateOrders(orders)
        }

        viewModel.error.observe(viewLifecycleOwner) { err ->
            if (!err.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Xatolik: $err", Toast.LENGTH_LONG).show()
            }
        }

        // SwipeRefresh Listener
        swipeRefreshLayout.setOnRefreshListener {
            fetchOrders()
            swipeRefreshLayout.isRefreshing = false
        }

        // Fragment ochilganda avtomatik yuklash
        swipeRefreshLayout.isRefreshing = true
        fetchOrders()
    }

    private fun fetchOrders() {
        val token = PrefsManager.getAuthToken(requireContext()).toString()
        viewModel.fetchOrders(token)
        swipeRefreshLayout.isRefreshing = false
    }
}
