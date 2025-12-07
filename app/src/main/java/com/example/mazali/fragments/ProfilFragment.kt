package com.example.mazali.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mazali.R
import com.example.mazali.data.network.RetrofitClient
import com.example.mazali.data.repository.AuthRepository
import com.example.mazali.ui.auth.viewmodel.AuthState
import com.example.mazali.ui.auth.viewmodel.AuthViewModel
import com.example.mazali.ui.auth.viewmodel.AuthViewModelFactory
import com.example.mazali.utils.PrefsManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlin.getValue

class ProfilFragment : Fragment() {
    private lateinit var userNameTv: TextView
    private lateinit var userAvatar: ImageView
    private lateinit var themeSwitch: Switch
    private lateinit var tahrirlashBtn: TextView
    private lateinit var ai_bot: TextView
    private lateinit var connectBtn: TextView
    private lateinit var filialBtn: TextView
    private lateinit var authBtn: TextView
    private lateinit var loadingOverlay: ConstraintLayout

    private val viewModel: AuthViewModel by viewModels {
        val repository = AuthRepository(RetrofitClient.authApi)
        AuthViewModelFactory(repository, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        userNameTv = view.findViewById(R.id.user_full_name)
        userAvatar = view.findViewById(R.id.base_avatar)
        themeSwitch = view.findViewById(R.id.theme_rejim)
        tahrirlashBtn = view.findViewById(R.id.tahrirlash_btn)
        ai_bot = view.findViewById(R.id.language_btn)
        connectBtn = view.findViewById(R.id.connect_btn)
        filialBtn = view.findViewById(R.id.filial_btn)
        authBtn = view.findViewById(R.id.auth_btn)
        loadingOverlay = view.findViewById(R.id.loadingOverlay_login)

        val prefs_ = requireContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val userName = prefs_.getString("USER_NAME", "")
        userNameTv.text = userName ?: ""

        val prefs = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        when (prefs.getString("avatar", "0")) {
            "0" -> userAvatar.setImageDrawable(null)
            "1" -> userAvatar.setImageResource(R.drawable.man)
            "2" -> userAvatar.setImageResource(R.drawable.woman)
        }

        // Theme switch
        val token = PrefsManager.getAuthToken(requireContext())
        if (token.hashCode() != "".hashCode()){
            authBtn.text = "Tizimdan chiqish"
            authBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_logout_24,0,0,0)
            authBtn.setOnClickListener {
                showSimpleAlertDialog(requireContext())
            }
        }else{
            authBtn.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.base_frame, LoginFragment())
                    ?.commit()
            }
        }
        val themePrefs = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        themeSwitch.isChecked = themePrefs.getBoolean("isDarkMode", false)
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            themePrefs.edit().putBoolean("isDarkMode", isChecked).apply()
            requireActivity().recreate()
        }

        // Buttonlar click listenerlari
        tahrirlashBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, TahrirlashFragment())
                ?.commit()
        }

        val bottomsheet = layoutInflater.inflate(R.layout.connect_sheet, null)
        val connectDialog = context?.let { BottomSheetDialog(it) }
        connectDialog?.setContentView(bottomsheet)
        connectBtn.setOnClickListener { connectDialog?.show() }

        filialBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, FilliallarFragment())
                ?.commit()
        }

        val languageDialogView = inflater.inflate(R.layout.language_sheet, null)
        val languageDialog = context?.let { BottomSheetDialog(it) }
        languageDialog?.setContentView(languageDialogView)
        ai_bot.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, ChatFragment())
                ?.commit()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthState.Idle -> {
                        loadingOverlay.visibility = View.GONE
                    }
                    is AuthState.Loading -> {
                        loadingOverlay.visibility = View.VISIBLE
                    }
                    is AuthState.Success -> {
                        loadingOverlay.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        val prefs_ = requireContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
                        val userName = prefs_.getString("USER_NAME", "")
                        userNameTv.text = userName ?: ""
                        authBtn.text = "Tizimga kirish"
                        authBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_login_24,0,0,0)
                        authBtn.setOnClickListener {
                            authBtn.setOnClickListener {
                                activity?.supportFragmentManager?.beginTransaction()
                                    ?.replace(R.id.base_frame, LoginFragment())
                                    ?.commit()
                            }
                        }
                    }
                    is AuthState.Error -> {
                        loadingOverlay.visibility = View.GONE
                        Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }
    private fun showSimpleAlertDialog(context:Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Diqqat")
        builder.setMessage("Tizimdan chiqishni istaysizmi?")

        builder.setPositiveButton("Ha") { dialog: DialogInterface, which: Int ->
            viewModel.logout()
            dialog.dismiss()
        }

        builder.setNegativeButton("Yo'q") { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }

        builder.setCancelable(false)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
