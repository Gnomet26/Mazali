package com.example.mazali.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mazali.R
import com.example.mazali.data.FoodItem
import com.example.mazali.utils.PrefsManager

class MahsulotFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_mahsulot, container, false)

        rootView.findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, MenyuFragment())
                ?.commit()
        }

        val imageProduct: ImageView = rootView.findViewById(R.id.imageProduct)
        val textProductName: TextView = rootView.findViewById(R.id.textProductName)
        val textPrice: TextView = rootView.findViewById(R.id.textPrice)
        val textDescription: TextView = rootView.findViewById(R.id.textDescription)
        val textQuantity: TextView = rootView.findViewById(R.id.textQuantity)
        val buttonMinus: ImageButton = rootView.findViewById(R.id.buttonMinus)
        val buttonPlus: ImageButton = rootView.findViewById(R.id.buttonPlus)
        val buttonAddToCart: Button = rootView.findViewById(R.id.buttonAddToCart)
        val textTotalPrice: TextView = rootView.findViewById(R.id.textTotalPrice)

        val productName = arguments?.getString("foodName")
        val productPriceString = arguments?.getString("foodPrice")
        val productImageRes = arguments?.getString("foodImage") ?: "none"
        val productCategory = arguments?.getString("foodCategory")
        val productDescription = arguments?.getString("foodDescription") ?: ""
        val productId = arguments?.getInt("foodID") ?: 0

        val unitPrice: Int = productPriceString?.filter { it.isDigit() }?.toIntOrNull() ?: 0

        textProductName.text = productName ?: "Noma'lum mahsulot"
        textPrice.text = productPriceString ?: "0 so'm"
        Glide.with(requireContext())
            .load(productImageRes)
            .into(imageProduct)
        textDescription.text = productDescription

        var qty = 1
        fun updateUI() {
            textQuantity.text = qty.toString()
            val total = unitPrice * qty
            textTotalPrice.text = "Jami: $total so'm"
            buttonAddToCart.text = "Savatchaga qo'shish — $qty ta"
            buttonMinus.isEnabled = qty > 1
            buttonMinus.alpha = if (qty > 1) 1f else 0.5f
        }

        buttonMinus.setOnClickListener {
            if (qty > 1) {
                qty--
                updateUI()
            }
        }

        buttonPlus.setOnClickListener {
            qty++
            updateUI()
        }

        // 🛒 Savatga qo‘shish
        buttonAddToCart.setOnClickListener {
            val cartList = PrefsManager.getCart(requireContext())

            val existingIndex = cartList.indexOfFirst { it.name == productName }
            if (existingIndex >= 0) {
                cartList[existingIndex].quantity += qty
            } else {
                val newItem = FoodItem(
                    id = productId,
                    name = productName ?: "Noma'lum",
                    price = productPriceString ?: "0 so'm",
                    image = productImageRes,
                    category = productCategory ?: "Kategoriya yo'q",
                    comment = productDescription,
                    quantity = qty
                )
                cartList.add(newItem)
            }

            PrefsManager.saveCart(requireContext(), cartList)

            Toast.makeText(
                requireContext(),
                "🛍 ${productName ?: "Mahsulot"} savatchaga qo‘shildi ($qty ta)",
                Toast.LENGTH_SHORT
            ).show()

            // 🧭 MenyuFragmentga qaytish
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, MenyuFragment())
                ?.commit()
        }

        updateUI()
        return rootView
    }
}
