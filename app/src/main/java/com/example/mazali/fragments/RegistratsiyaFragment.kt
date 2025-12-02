package com.example.mazali.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mazali.R
import com.example.mazali.data.repository.AuthRepository
import com.example.mazali.data.network.RetrofitClient
import com.example.mazali.ui.auth.viewmodel.AuthState
import com.example.mazali.ui.auth.viewmodel.AuthViewModel
import com.example.mazali.ui.auth.viewmodel.AuthViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest

class RegistratsiyaFragment : Fragment() {

    private lateinit var phoneEdit: TextInputEditText
    private lateinit var fullNameEdit: TextInputEditText
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var confirmPasswordEdit: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var loadingOverlay: ConstraintLayout
    private lateinit var back_btn: ImageView

    private val viewModel: AuthViewModel by viewModels {
        val repository = AuthRepository(RetrofitClient.authApi)
        AuthViewModelFactory(repository, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registratsiya, container, false)

        // ---------- Views ----------
        phoneEdit = view.findViewById(R.id.editPhone)
        fullNameEdit = view.findViewById(R.id.editFullName)
        passwordEdit = view.findViewById(R.id.editPassword)
        confirmPasswordEdit = view.findViewById(R.id.editConfirmPassword)
        registerButton = view.findViewById(R.id.buttonRegister)
        loginLink = view.findViewById(R.id.textLoginLink)
        loadingOverlay = view.findViewById(R.id.loadingOverlay)
        back_btn = view.findViewById(R.id.register_back)

        back_btn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.base_frame, RootFragment())
                .commit()
        }

        // 📱 Telefon raqamiga fokus bo‘lsa +998
        phoneEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && phoneEdit.text.isNullOrEmpty()) {
                phoneEdit.setText("+998")
                phoneEdit.setSelection(phoneEdit.text!!.length)
            }
        }

        // 📝 Login oynasiga o'tish
        loginLink.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.base_frame, LoginFragment())
                .commit()
        }

        // 🚀 Register tugmasi
        registerButton.setOnClickListener {
            val phone = phoneEdit.text.toString().trim()
            val fullName = fullNameEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()
            val confirmPassword = confirmPasswordEdit.text.toString().trim()

            if (phone.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Iltimos barcha maydonlarni to'ldiring")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showToast("Parollar mos emas!")
                return@setOnClickListener
            }

            // ViewModel orqali ro'yxatdan o'tish
            viewModel.register(fullName, phone, password)
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

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
