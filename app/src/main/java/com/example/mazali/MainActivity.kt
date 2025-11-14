package com.example.mazali

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mazali.fragments.RootFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // 📌 1. Tema rejimini SharedPreferences dan oldin yuklab olish
        val prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
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
}
