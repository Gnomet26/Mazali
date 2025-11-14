package com.example.mazali.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.adapters.CartAdapter
import com.example.mazali.data.FoodItem
import com.example.mazali.data.Order
import com.example.mazali.utils.PrefsManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SavatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textTotal: TextView
    private lateinit var buttonOrder: Button
    private lateinit var emptyText: TextView

    private lateinit var cartList: MutableList<FoodItem>
    private lateinit var cartAdapter: CartAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_savat, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCart)
        textTotal = view.findViewById(R.id.textTotalPrice)
        buttonOrder = view.findViewById(R.id.buttonOrder)
        emptyText = view.findViewById(R.id.textEmptyCart)

        // 🛒 Cart ma'lumotlarini olish
        context?.let { cartList = PrefsManager.getCart(it) }

        // 🧾 Adapter
        cartAdapter = CartAdapter(cartList, object : CartAdapter.CartListener {
            override fun onQuantityChanged() {
                PrefsManager.saveCart(requireContext(), cartList)
                updateTotal()
                toggleEmptyView()
            }

            override fun onItemDeleted(position: Int) {
                PrefsManager.saveCart(requireContext(), cartList)
                updateTotal()
                toggleEmptyView()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = cartAdapter

        // 🟡 Buyurtma berish tugmasi
        buttonOrder.setOnClickListener {
            if (cartList.isEmpty()) {
                Toast.makeText(requireContext(), "Savat bo‘sh!", Toast.LENGTH_SHORT).show()
            } else {
                showCheckoutBottomSheet()
            }
        }

        updateTotal()
        toggleEmptyView()
        return view
    }

    // ✅ BottomSheet — Rasmiylashtirish oynasi
    private fun showCheckoutBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheet_checkout, null)

        val editAddress = bottomSheetView.findViewById<EditText>(R.id.editAddress)
        val radioGroup = bottomSheetView.findViewById<RadioGroup>(R.id.radioGroupPayment)
        val buttonCheckout = bottomSheetView.findViewById<Button>(R.id.buttonCheckout)

        buttonCheckout.setOnClickListener {
            val address = editAddress.text.toString().trim()
            if (address.isEmpty()) {
                Toast.makeText(requireContext(), "Manzilni kiriting!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(requireContext(), "To‘lov usulini tanlang!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val paymentMethod =
                bottomSheetView.findViewById<RadioButton>(selectedId).text.toString()

            // 💰 Jami narxni hisoblash
            val totalPrice = cartList.sumOf {
                it.price.filter { c -> c.isDigit() }.toInt() * it.quantity
            }

            // 📝 Buyurtma yaratish
            val order = Order(
                id = System.currentTimeMillis().toString(),
                date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date()),
                totalPrice = totalPrice,
                status = "Yetkazib berilyapti",
                address = address,
                paymentMethod = paymentMethod,
                products = cartList.toList()
            )

            // 💾 Buyurtmani saqlash
            PrefsManager.saveOrder(requireContext(), order)

            // 🧹 Savatni tozalash
            PrefsManager.clearCart(requireContext())
            cartList.clear()
            cartAdapter.notifyDataSetChanged()
            updateTotal()
            toggleEmptyView()

            // 📢 Xabar
            Toast.makeText(requireContext(), "Buyurtma rasmiylashtirildi ✅", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    // ✅ Jami narxni hisoblash
    private fun updateTotal() {
        val total = cartList.sumOf {
            it.price.filter { c -> c.isDigit() }.toInt() * it.quantity
        }
        textTotal.text = "Jami: $total so'm"
    }

    // ✅ Bo‘sh savatni ko‘rsatish
    private fun toggleEmptyView() {
        if (cartList.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            buttonOrder.isEnabled = false
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            buttonOrder.isEnabled = true
        }
    }
}
