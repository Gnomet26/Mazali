package com.example.mazali.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.Fragment

import com.example.mazali.R

class RegistratsiyaFragment : Fragment() {

    private lateinit var phoneEdit: TextInputEditText
    private lateinit var fullNameEdit: TextInputEditText
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var confirmPasswordEdit: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registratsiya, container, false)

        phoneEdit = view.findViewById(R.id.editPhone)
        fullNameEdit = view.findViewById(R.id.editFullName)
        passwordEdit = view.findViewById(R.id.editPassword)
        confirmPasswordEdit = view.findViewById(R.id.editConfirmPassword)
        registerButton = view.findViewById(R.id.buttonRegister)
        loginLink = view.findViewById(R.id.textLoginLink)

        // 📱 Telefon raqamiga focus bo'lsa +998 ni avtomatik chiqarish
        phoneEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && phoneEdit.text.isNullOrEmpty()) {
                phoneEdit.setText("+998")
                phoneEdit.setSelection(phoneEdit.text!!.length)
            }
        }

        // 📝 Login oynasiga o'tish
        loginLink.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.base_frame, LoginFragment())
                ?.commit()
        }

        // 🚀 Register tugmasi bosilganda
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

            // 🔐 Bu yerda ro'yxatdan o'tish logikasini qo'shish mumkin (server yoki local)
            showToast("Ro'yxatdan muvaffaqiyatli o'tdingiz ✅")
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.base_frame, LoginFragment())
                ?.commit()
        }

        return view
    }

    private fun showToast(msg: String) {
        android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show()
    }
}
