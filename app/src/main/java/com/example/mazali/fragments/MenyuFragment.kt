package com.example.mazali.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mazali.R
import com.example.mazali.adapters.ProductAdapter
import com.example.mazali.data.FoodItem
import com.example.mazali.data.repository.ProductRepository
import com.example.mazali.network.RetrofitClient2
import com.example.mazali.ui.auth.viewmodel.ProductViewModel
import com.example.mazali.ui.auth.viewmodel.ProductViewModelFactory
import com.google.android.material.button.MaterialButton

class MenyuFragment : Fragment() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<FoodItem>()

    private lateinit var viewModel: ProductViewModel

    private var isLoading = false
    private var currentPage = 1
    private var isLastPage = false

    private var selectedCategory: String? = null
    private var currentSearchQuery: String? = null

    private lateinit var searchInput: EditText

    private lateinit var btnAll: MaterialButton
    private lateinit var btnCombo: MaterialButton
    private lateinit var btnBurger: MaterialButton
    private lateinit var btnShaurma: MaterialButton
    private lateinit var btnLavash: MaterialButton
    private lateinit var btnSouslar: MaterialButton
    private lateinit var btnDrinks: MaterialButton
    private lateinit var btnNotification: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menyu, container, false)

        recyclerView = view.findViewById(R.id.menyu_recyclerview)
        swipeRefreshLayout = view.findViewById(R.id.menu_refresh)
        searchInput = view.findViewById(R.id.etSearch)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        val repository = ProductRepository(RetrofitClient2.instance)
        val factory = ProductViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        // buttons
        btnAll = view.findViewById(R.id.btnAll)
        btnCombo = view.findViewById(R.id.btnCombo)
        btnBurger = view.findViewById(R.id.btnBurger)
        btnShaurma = view.findViewById(R.id.btnShaurma)
        btnLavash = view.findViewById(R.id.btnLavash)
        btnSouslar = view.findViewById(R.id.btnSouslar)
        btnDrinks = view.findViewById(R.id.btnDrinks)

        btnNotification = view.findViewById(R.id.notification_btn)

        btnNotification.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, BildirishnomaFragment())
                ?.commit()
        }

        // CATEGORY mapping
        val categoryMap = mapOf(
            btnAll to null,
            btnCombo to "combo",
            btnBurger to "burger",
            btnShaurma to "shaurma",
            btnLavash to "lavash",
            btnSouslar to "souslar",
            btnDrinks to "drinks"
        )


// CATEGORY clicks
        categoryMap.forEach { (button, category) ->
            button.setOnClickListener {
                selectedCategory = category
                currentSearchQuery = null
                searchInput.setText("")
                currentPage = 1
                isLastPage = false
                adapter.clearList()

                updateCategoryButtonColors(button as MaterialButton)  // 🔥 rangni yangilash

                if (category == null) viewModel.fetchProducts(1)
                else viewModel.fetchCategoryProducts(category)
            }
        }


        // SEARCH listener
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val query = text.toString().trim()

                if (query.isEmpty()) {
                    if (selectedCategory == null) {
                        currentSearchQuery = null
                        selectedCategory = null
                        adapter.clearList()
                        viewModel.fetchProducts(1)
                    }
                    return
                }

                selectedCategory = null
                currentSearchQuery = query
                currentPage = 1
                isLastPage = false
                adapter.clearList()

                viewModel.fetchSearchProducts(query)
            }
        })

        // OBSERVERS
        viewModel.products.observe(viewLifecycleOwner) { products ->
            swipeRefreshLayout.isRefreshing = false
            isLoading = false

            if (currentPage == 1) adapter.updateList(products)
            else adapter.appendList(products)

            if (products.isEmpty()) isLastPage = true
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            swipeRefreshLayout.isRefreshing = false
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        // PAGINATION
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !isLoading && !isLastPage) {
                    val visible = layoutManager.childCount
                    val total = layoutManager.itemCount
                    val first = layoutManager.findFirstVisibleItemPosition()

                    if (visible + first >= total && first >= 0) {
                        isLoading = true
                        currentPage++

                        currentSearchQuery?.let {
                            viewModel.fetchSearchNextPage(it)
                            return
                        }

                        selectedCategory?.let {
                            viewModel.fetchCategoryNextPage(it)
                            return
                        }

                        viewModel.fetchNextPage()
                    }
                }
            }
        })

        // 🔥 SWIPE REFRESH
        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            isLastPage = false
            adapter.clearList()

            // search ustuvor
            currentSearchQuery?.let {
                viewModel.fetchSearchProducts(it)
                return@setOnRefreshListener
            }

            // category keyin
            selectedCategory?.let {
                viewModel.fetchCategoryProducts(it)
                return@setOnRefreshListener
            }

            // default
            viewModel.fetchProducts(1)
        }

        // Initial data
        isLoading = true
        viewModel.fetchProducts(1)

        return view
    }

    private fun updateCategoryButtonColors(selectedButton: MaterialButton) {
        val buttons = listOf(btnAll, btnCombo, btnBurger, btnShaurma, btnLavash, btnSouslar, btnDrinks)
        buttons.forEach { button ->
            if (button == selectedButton) {
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                button.setTextColor(Color.WHITE)
            } else {
                button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                button.setTextColor(Color.BLACK)
            }
        }
    }
}
