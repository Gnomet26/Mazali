package com.example.mazali.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.adapters.NotificationAdapter
import com.example.mazali.data.NotificationItem

class BildirishnomaFragment : Fragment() {
    lateinit var view__: View
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view__ = inflater.inflate(R.layout.fragment_bildirishnoma, container, false)

        view__.findViewById<ImageView>(R.id.not_back_btn).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.base_frame, RootFragment())
                ?.commit()
        }

        recyclerView = view__.findViewById(R.id.recyclerNotifications)

        val fakeNotifications = listOf(
            NotificationItem("Yangi aksiya!", "Bugun barcha burgerlarga 30% chegirma!", "12:45", R.drawable.baseline_notifications_24),
            NotificationItem("Buyurtma qabul qilindi", "Sizning 000123 raqamli buyurtmangiz qabul qilindi.", "12:30", R.drawable.baseline_notifications_24),
            NotificationItem("Yetkazib berildi", "Buyurtmangiz manzilingizga yetkazildi. Yoqimli ishtaha!", "12:15", R.drawable.baseline_notifications_24),
            NotificationItem("Bonus ballar", "Sizga 200 bonus ball qo‘shildi!", "12:00", R.drawable.baseline_notifications_24)
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = NotificationAdapter(fakeNotifications)

        return view__
    }

}