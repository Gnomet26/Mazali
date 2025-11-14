package com.example.mazali.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mazali.R
import com.google.android.material.button.MaterialButton

class LoginFragment : Fragment() {
    private lateinit var editPhone: EditText
    private lateinit var editPassword: EditText
    private lateinit var textRegister: TextView
    private lateinit var buttonLogin: MaterialButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        editPhone = view.findViewById(R.id.editPhone)
        editPassword = view.findViewById(R.id.editPassword)
        textRegister = view.findViewById(R.id.textRegister)
        buttonLogin = view.findViewById(R.id.buttonLogin)

        // 📞 Fokus bo‘lganda +998 chiqib qoladi
        editPhone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editPhone.text.isEmpty()) {
                editPhone.setText("+998")
                editPhone.setSelection(editPhone.text.length) // kursorni ohiriga qo‘yish
            }
        }


        // 🧭 Ro‘yxatdan o‘tish fragmentiga o‘tish
        textRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.base_frame, RegistratsiyaFragment())
                .commit()
        }

        // 🚀 Login bosilganda
        buttonLogin.setOnClickListener {
            val phone = editPhone.text.toString().trim()
            val password = editPassword.text.toString().trim()

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.base_frame, RootFragment())
                ?.commit()
        }

        return view
    }
}
