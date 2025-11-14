package com.example.mazali.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mazali.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class RootFragment : Fragment() {

    private lateinit var rootView: View
    private var window_ = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_root, container, false)
        val bottomNav = rootView.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // 📌 1. SharedPreferences orqali oxirgi tanlangan itemni yuklab olish
        val prefs = requireContext().getSharedPreferences("nav_prefs", Context.MODE_PRIVATE)
        val savedItemId = prefs.getInt("selected_item_id", R.id.item_home)
        bottomNav.selectedItemId = savedItemId

        // 📌 2. savedItemId ga mos fragmentni yuklash
        when (savedItemId) {
            R.id.item_home -> {
                openFragment(MenyuFragment())
                window_ = 0
            }
            R.id.item_savat -> {
                openFragment(SavatFragment())
                window_ = 1
            }
            R.id.item_buyuritma -> {
                openFragment(BuyuritmalarFragment())
                window_ = 2
            }
            R.id.item_sozlama -> {
                openFragment(ProfilFragment())
                window_ = 3
            }
        }

        // 📌 3. BottomNavigation listener
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    if (window_ != 0) {
                        openFragment(MenyuFragment())
                        window_ = 0
                    }
                    saveSelectedItemId(item.itemId)
                    true
                }
                R.id.item_savat -> {
                    if (window_ != 1) {
                        openFragment(SavatFragment())
                        window_ = 1
                    }
                    saveSelectedItemId(item.itemId)
                    true
                }
                R.id.item_buyuritma -> {
                    if (window_ != 2) {
                        openFragment(BuyuritmalarFragment())
                        window_ = 2
                    }
                    saveSelectedItemId(item.itemId)
                    true
                }
                R.id.item_sozlama -> {
                    if (window_ != 3) {
                        openFragment(ProfilFragment())
                        window_ = 3
                    }
                    saveSelectedItemId(item.itemId)
                    true
                }
                else -> false
            }
        }

        return rootView
    }

    // 📌 Kichik helper funksiya — fragmentlarni ochish uchun
    private fun openFragment(fragment: Fragment) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.user_frame, fragment)
            ?.commit()
    }

    // 📌 Tanlangan item IDni SharedPreferences ga saqlash
    private fun saveSelectedItemId(itemId: Int) {
        val prefs = requireContext().getSharedPreferences("nav_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("selected_item_id", itemId).apply()
    }
}
