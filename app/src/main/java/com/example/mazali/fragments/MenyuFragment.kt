package com.example.mazali.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.adapters.MenuAdapter
import com.example.mazali.data.FoodItem
import com.google.firebase.messaging.FirebaseMessaging

class MenyuFragment : Fragment() {
    lateinit var view__: View
    lateinit var test_p: SharedPreferences
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var recyclerMenu: RecyclerView
    private lateinit var categoryContainer: LinearLayout
    private lateinit var editSearch: EditText
    private var currentCategory = "Barchasi"
    private var currentQuery = ""
    val foodList = listOf(
        FoodItem(
            name = "Sinfdosh Combo 1",
            price = "25 000 so'm",
            imageResId = R.drawable.img1,
            category = "Combo",
            description = "Issiq va xushbo'y xot-dog, qarsildoq kartoshka fri, shirali naggetslar 2 ta va bir stakan soflantiruvchi Pepsi (0,3 l). Har bir maktab o'quvchisini xursand qiladigan kombo."
        ),FoodItem(
            name = "Sinfdosh Combo 2",
            price = "27 000 so'm",
            imageResId = R.drawable.img2,
            category = "Combo",
            description = "Issiq va xushbo'y xot-dog, qarsildoq kartoshka fri, shirali naggetslar 2 ta va bir stakan soflantiruvchi Pepsi (0,3 l). Har bir maktab o'quvchisini xursand qiladigan kombo."
        ),FoodItem(
            name = "Lavash Time Combo 1",
            price = "25 000 so'm",
            imageResId = R.drawable.img3,
            category = "Combo",
            description = "Tovuqli ishtaha ochar mini lavash, qarsildoq mini kartoshka fri, xushbo'y ketchup va bir stakan soflantiruvchi Pepsi (0,3 l). Klassikani haqiqiy qadrlovchilari uchun!"
        ),FoodItem(
            name = "Lavash Time Combo 2",
            price = "25 000 so'm",
            imageResId = R.drawable.img4,
            category = "Combo",
            description = "Tovuqli ishtaha ochar mini lavash, qarsildoq mini kartoshka fri, xushbo'y ketchup va bir stakan soflantiruvchi Pepsi (0,3 l). Klassikani haqiqiy qadrlovchilari uchun!"
        ),
    )


    private val categoryList = listOf(
        "Barchasi",
        "Combo",
        "Burgerlar",
        "Shaurma",
        "Lavash",
        "Souslar",
        "Ichimliklar"

    )

    private var selectedButton: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view__ = inflater.inflate(R.layout.fragment_menyu, container, false)

        // Sinov uchun bir nechta fake taomlar
        editSearch = view__.findViewById(R.id.editSearch)
        recyclerMenu = view__.findViewById(R.id.menyu_recyclerview)
        categoryContainer = view__.findViewById(R.id.categoryContainer)

        setupCategoryButtons()

        recyclerMenu.layoutManager = GridLayoutManager(requireContext(), 2)
        menuAdapter = MenuAdapter(foodList)
        recyclerMenu.adapter = menuAdapter

        setupSearchListener()

        view__.findViewById<ImageView>(R.id.notification_btn).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.base_frame, BildirishnomaFragment())
                ?.commit()
        }

        return view__
    }

    private fun setupCategoryButtons() {
        for (category in categoryList) {
            val button = Button(requireContext()).apply {
                text = category
                setPadding(40, 10, 40, 10)
                background = ContextCompat.getDrawable(requireContext(), R.drawable.category_button_selector)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                isAllCaps = false
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 16, 0)
            button.layoutParams = params

            button.setOnClickListener {
                selectCategory(button, category)
            }

            categoryContainer.addView(button)

            // Boshlang‘ich tanlov: “Barchasi”
            if (category == "Barchasi") {
                selectCategory(button, category)
            }
        }
    }

    private fun selectCategory(button: Button, category: String) {
        selectedButton?.isSelected = false
        selectedButton?.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))

        button.isSelected = true
        button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        selectedButton = button

        val filteredList = if (category == "Barchasi") {
            foodList
        } else {
            foodList.filter { it.category == category }
        }

        menuAdapter = MenuAdapter(filteredList)
        recyclerMenu.adapter = menuAdapter
    }

    private fun setupSearchListener() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s.toString()
                filterMenu()
            }
        })
    }

    private fun filterMenu() {
        var filteredList = foodList

        // Avval kategoriya bo‘yicha filter
        if (currentCategory != "Barchasi") {
            filteredList = filteredList.filter { it.category == currentCategory }
        }

        // Keyin izlash bo‘yicha filter
        if (currentQuery.isNotEmpty()) {
            filteredList = filteredList.filter {
                it.name.contains(currentQuery, ignoreCase = true)
            }
        }

        menuAdapter = MenuAdapter(filteredList)
        recyclerMenu.adapter = menuAdapter
    }
}