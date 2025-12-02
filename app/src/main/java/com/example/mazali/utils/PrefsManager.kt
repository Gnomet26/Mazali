package com.example.mazali.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.mazali.data.FoodItem
import com.example.mazali.data.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PrefsManager {

    // ---------- Cart ----------
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

    // ---------- Orders ----------
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

    // ---------- Auth (Yangi qo'shilgan) ----------
    private const val AUTH_PREFS = "AUTH_DATA"
    private const val KEY_DEVICE_TOKEN = "DEVICE_TOKEN"
    private const val KEY_AUTH_TOKEN = "AUTH_TOKEN"

    private fun getAuthPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE)
    }

    fun saveDeviceToken(context: Context, token: String) {
        getAuthPrefs(context).edit().putString(KEY_DEVICE_TOKEN, token).apply()
    }

    fun getDeviceToken(context: Context): String? {
        return getAuthPrefs(context).getString(KEY_DEVICE_TOKEN, null)
    }

    fun saveAuthToken(context: Context, token: String) {
        getAuthPrefs(context).edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun getAuthToken(context: Context): String? {
        return getAuthPrefs(context).getString(KEY_AUTH_TOKEN, null)
    }

    fun clearAuthData(context: Context) {
        getAuthPrefs(context).edit().clear().apply()
    }
    // ---------- USER DATA ----------
    private const val USER_PREFS = "USER_DATA"
    private const val KEY_USER_NAME = "USER_NAME"
    private const val KEY_USER_PHONE = "USER_PHONE"

    fun saveUserData(context: Context, name: String?, phone: String?) {
        val prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_PHONE, phone)
            apply()
        }
    }

    fun getUserName(context: Context): String? {
        return context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
            .getString(KEY_USER_NAME, "")
    }

    fun getUserPhone(context: Context): String? {
        return context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
            .getString(KEY_USER_PHONE, "")
    }
    fun clearUserDate(context: Context) {
        val prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_USER_NAME, "")
        editor.putString(KEY_USER_PHONE, "")
        editor.apply()
    }

}
