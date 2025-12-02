package com.example.mazali

import android.Manifest
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mazali.fragments.RootFragment
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    lateinit var user_data_prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        // 📌 1. Tema rejimini SharedPreferences dan oldin yuklab olish
        user_data_prefs = getSharedPreferences("AUTH_DATA", MODE_PRIVATE)
        val prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("isDarkMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 📌 2. System insetlarni sozlash
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 📌 3. Boshlang‘ich fragmentni yuklash (faqat birinchi marta)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.base_frame, RootFragment())
                .commit()
        }

        // 📌 4. Status bar / nav bar ranglarini qo‘llash
        applySystemBarColors()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }
        getFirebaseToken()

    }

    override fun onResume() {
        super.onResume()
        // Har safar qaytganda ham tekshirib yangilab qo'yamiz
        applySystemBarColors()
    }

    private fun applySystemBarColors() {
        val isNightMode = resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES

        if (isNightMode) {
            window.statusBarColor = getColor(R.color.colorPrimaryNight)
            window.navigationBarColor = getColor(R.color.backgroundDark)
            window.decorView.systemUiVisibility = 0 // oq iconlar (dark)
        } else {
            window.statusBarColor = getColor(R.color.colorPrimaryDark)
            window.navigationBarColor = getColor(R.color.backgroundLight)
            // qora iconlar (light)
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or
                        android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    private fun getFirebaseToken() {
        val user_auth_edit = user_data_prefs.edit()
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    user_auth_edit.putString("DEVICE_TOKEN", null)
                    user_auth_edit.commit()
                    user_auth_edit.apply()
                    return@addOnCompleteListener
                }

                val token = task.result
                user_auth_edit.putString("DEVICE_TOKEN", token)
                user_auth_edit.commit()
                user_auth_edit.apply()
            }
    }
}
