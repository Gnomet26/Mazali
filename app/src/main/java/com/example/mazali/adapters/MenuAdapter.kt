package com.example.mazali.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.data.FoodItem
import com.example.mazali.fragments.MahsulotFragment

class MenuAdapter(private val foodList: List<FoodItem>) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.imageFood)
        val foodName: TextView = itemView.findViewById(R.id.textFoodName)
        val foodPrice: TextView = itemView.findViewById(R.id.textFoodPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menyu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val food = foodList[position]

        holder.foodName.text = food.name
        holder.foodPrice.text = food.price
        holder.foodImage.setImageResource(food.imageResId)

        holder.itemView.setOnClickListener { view ->

            val bundle = Bundle().apply {
                putString("foodName", food.name)
                putString("foodPrice", food.price)
                putInt("foodImage", food.imageResId)
                putString("foodCategory", food.category)
                putString("foodDescription", food.description)
            }

            // 🔄 ProductFragmentga o‘tish
            val activity = view.context as FragmentActivity
            activity.supportFragmentManager.commit {
                replace(R.id.user_frame, MahsulotFragment::class.java, bundle)

            }
        }
    }

    override fun getItemCount(): Int = foodList.size
}
