package com.example.mazali.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mazali.R
import com.example.mazali.data.repository.AuthRepository
import com.example.mazali.data.network.RetrofitClient
import com.example.mazali.ui.auth.viewmodel.AuthState
import com.example.mazali.ui.auth.viewmodel.AuthViewModel
import com.example.mazali.ui.auth.viewmodel.AuthViewModelFactory
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collectLatest

class LoginFragment : Fragment() {

    private lateinit var editPhone: EditText
    private lateinit var editPassword: EditText
    private lateinit var textRegister: TextView
    private lateinit var buttonLogin: MaterialButton
    private lateinit var loadingOverlay: ConstraintLayout
    private lateinit var back_btn: ImageView

    // ViewModel tayyorlash
    private val viewModel: AuthViewModel by viewModels {
        val repository = AuthRepository(RetrofitClient.authApi)
        AuthViewModelFactory(repository, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // ---------- Views ----------
        editPhone = view.findViewById(R.id.editPhone)
        editPassword = view.findViewById(R.id.editPassword)
        textRegister = view.findViewById(R.id.textRegister)
        buttonLogin = view.findViewById(R.id.buttonLogin)
        loadingOverlay = view.findViewById(R.id.loadingOverlay)

        back_btn = view.findViewById(R.id.login_back)
        back_btn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.base_frame, RootFragment())
                .commit()
        }

        // 📞 Telefon raqamiga fokus bo‘lganda +998
        editPhone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editPhone.text.isEmpty()) {
                editPhone.setText("+998")
                editPhone.setSelection(editPhone.text.length)
            }
        }

        // 🧭 Ro‘yxatdan o‘tish link
        textRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.base_frame, RegistratsiyaFragment())
                .commit()
        }

        // 🚀 Login button
        buttonLogin.setOnClickListener {
            val phone = editPhone.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Iltimos barcha maydonlarni to'ldiring", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ViewModel orqali login
            viewModel.login(phone, password)
        }

        // ---------- AuthState observer ----------
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
                        // ProfilFragment ga o'tish
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.base_frame, RootFragment())
                            .commit()
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
}
