package com.example.mazali.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.data.Branch
import com.example.mazali.ui.adapters.BranchAdapter

class FilliallarFragment : Fragment() {

    private lateinit var view__: View
    private lateinit var adapter: BranchAdapter

    // ❗ Misol uchun branchlar ro‘yxati
    private val sampleBranches = listOf(
        Branch("1", "Qoraqalpog‘iston Respublikasi", "Nukus, Oybek ko'chasi 1"),
        Branch("2","Toshkent viloyati", "Toshkent, Amir Temur ko'chasi 12"),
        Branch("3", "Samarqand viloyati", "Samarqand, Registon ko'chasi 5"),
        Branch("4", "Buxoro viloyati", "Buxoro, Zarafshon ko'chasi 24"),
        Branch("5", "Xorazm viloyati", "Urganch, Sh. Rashidov 8"),
        Branch("6", "Namangan viloyati", "Namangan, Mirzo Ulug'bek 10"),
        Branch("7", "Farg'ona viloyati", "Farg'ona, O‘zbekiston 3"),
        Branch("8", "Andijon viloyati", "Andijon, Shahrixon 7"),
        Branch("9", "Navoiy viloyati", "Navoiy, Shohod rey 2"),
        Branch("10", "Jizzax viloyati", "Jizzax, Ko‘cha 18"),
        Branch("11", "Sirdaryo viloyati", "Guliston, Rudaki 9"),
        Branch("12", "Qashqadaryo viloyati", "Qarshi, Mustaqillik 21")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view__ = inflater.inflate(R.layout.fragment_filliallar, container, false)

        // 🔙 Orqaga qaytish tugmasi
        view__.findViewById<ImageView>(R.id.filial_back).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, ProfilFragment())
                ?.commit()
        }

        // 🔸 Adapterni initialize qilish
        adapter = BranchAdapter { branch ->
            
        }

        // 🔸 RecyclerView sozlash
        val rv = view__.findViewById<RecyclerView>(R.id.rvBranches)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // 🔸 Ajratgich qo‘shish (Divider)
        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        rv.addItemDecoration(divider)

        // 🔸 Ma’lumotlarni yuklash
        adapter.submitList(sampleBranches)

        return view__
    }
}
