package com.example.mazali.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.mazali.data.FoodItem
import com.example.mazali.data.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PrefsManager {
    private const val PREF_NAME = "cart_prefs"
    private const val CART_KEY = "cart_items"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveCart(context: Context, cartList: MutableList<FoodItem>) {
        val json = Gson().toJson(cartList)
        getPrefs(context).edit().putString(CART_KEY, json).apply()
    }

    fun getCart(context: Context): MutableList<FoodItem> {
        val json = getPrefs(context).getString(CART_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<FoodItem>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    fun clearCart(context: Context) {
        getPrefs(context).edit().remove(CART_KEY).apply()
    }

    fun saveOrder(context: Context, order: Order) {
        val prefs = context.getSharedPreferences("orders", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("orders_list", "[]")
        val type = object : TypeToken<MutableList<Order>>() {}.type
        val list: MutableList<Order> = gson.fromJson(json, type)
        list.add(order)
        prefs.edit().putString("orders_list", gson.toJson(list)).apply()
    }

    fun getOrders(context: Context): MutableList<Order> {
        val prefs = context.getSharedPreferences("orders", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("orders_list", "[]")
        val type = object : TypeToken<MutableList<Order>>() {}.type
        return gson.fromJson(json, type)
    }
}
