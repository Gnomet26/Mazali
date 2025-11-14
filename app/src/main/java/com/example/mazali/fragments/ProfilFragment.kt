package com.example.mazali.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mazali.R

class ProfilFragment : Fragment() {

    private lateinit var view__: View
    private lateinit var prefs: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view__ = inflater.inflate(R.layout.fragment_profil, container, false)

        activity?.let { prefs = it.getSharedPreferences("user_data", Context.MODE_PRIVATE) }

        val themeSwitch = view__.findViewById<Switch>(R.id.theme_rejim)

        try {
            if (prefs.getString("avatar","0").toString().hashCode() == "0".hashCode()){
                view__.findViewById<ImageView>(R.id.base_avatar).setImageDrawable(null)
            }else if(prefs.getString("avatar","0").toString().hashCode() == "1".hashCode()){
                view__.findViewById<ImageView>(R.id.base_avatar).setImageResource(R.drawable.man)
            }else if(prefs.getString("avatar","0").toString().hashCode() == "2".hashCode()){
                view__.findViewById<ImageView>(R.id.base_avatar).setImageResource(R.drawable.woman)
            }

            if(prefs.getString("user_name","null").toString().hashCode() == "null".hashCode()){
                view__.findViewById<TextView>(R.id.user_full_name).text = ""
            }else{
                view__.findViewById<TextView>(R.id.user_full_name).text = prefs.getString("user_name","null").toString()
            }
        } catch (e: Exception){
            view__.findViewById<ImageView>(R.id.base_avatar).setImageDrawable(null)
            view__.findViewById<TextView>(R.id.user_full_name).text = ""
        }

        // 📌 SharedPreferences orqali saqlangan rejimni yuklash
        val prefs = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("isDarkMode", false)
        themeSwitch.isChecked = isDarkMode

        // 📌 Switch o'zgartirilganda — rejimni almashtirish
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Holatni saqlab qo'yamiz
            prefs.edit().putBoolean("isDarkMode", isChecked).apply()

            // Activity qayta yuklanadi → ranglar yangilanadi
            requireActivity().recreate()
        }

        // 📌 Profilni tahrirlash bosilganda fragmentni almashtirish
        view__.findViewById<TextView>(R.id.tahrirlash_btn).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, TahrirlashFragment())
                ?.commit()
        }

        val bottomsheet = layoutInflater.inflate(R.layout.connect_sheet, null)
        val dialog = context?.let { com.google.android.material.bottomsheet.BottomSheetDialog(it) }
        dialog?.setContentView(bottomsheet)

        view__.findViewById<TextView>(R.id.connect_btn).setOnClickListener {
            dialog?.show()
        }

        view__.findViewById<TextView>(R.id.filial_btn).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, FilliallarFragment())
                ?.commit()
        }

        val language_dialog = inflater.inflate(R.layout.language_sheet, null)
        val lan_dialog = context?.let { com.google.android.material.bottomsheet.BottomSheetDialog(it) }
        lan_dialog?.setContentView(language_dialog)

        view__.findViewById<TextView>(R.id.language_btn).setOnClickListener {
            lan_dialog?.show()
        }

        view__.findViewById<TextView>(R.id.auth_btn).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.base_frame, LoginFragment())
                ?.commit()
        }

        return view__
    }
}
