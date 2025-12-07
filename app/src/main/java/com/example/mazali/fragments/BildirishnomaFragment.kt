package com.example.mazali.fragments

import com.example.mazali.R
import com.example.mazali.adapters.NotificationAdapter
import com.example.mazali.data.network.ApiService2
import com.example.mazali.data.network.RetrofitClient3
import com.example.mazali.data.repository.NotificationRepository
import com.example.mazali.ui.auth.viewmodel.NotificationViewModel
import com.example.mazali.ui.auth.viewmodel.NotificationViewModelFactory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mazali.utils.PrefsManager

class BildirishnomaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var adapter: NotificationAdapter
    private lateinit var viewModel: NotificationViewModel
    private lateinit var backBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bildirishnoma, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvNotifications)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        backBtn = view.findViewById(R.id.not_back_btn)

        backBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, MenyuFragment())
                ?.commit()
        }

        val userToken = PrefsManager.getAuthToken(requireContext())
        val apiService = RetrofitClient3.getRetrofit(userToken.toString()).create(ApiService2::class.java)
        val repository = NotificationRepository(apiService)
        val factory = NotificationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[NotificationViewModel::class.java]

        // Adapter yaratish va item click orqali mark as read chaqirish
        adapter = NotificationAdapter(requireContext(), mutableListOf()) { notification ->
            viewModel.markNotificationAsRead(notification.id)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // SwipeRefresh
        swipeRefresh.setOnRefreshListener {
            viewModel.fetchNotifications()
        }

        // LiveData kuzatish
        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            adapter.updateList(notifications)
            swipeRefresh.isRefreshing = false
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
            swipeRefresh.isRefreshing = false
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            swipeRefresh.isRefreshing = isLoading
        }

        // Dastlabki ma’lumotni olish
        viewModel.fetchNotifications()
    }
}
