package com.example.mazali.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mazali.R
import com.example.mazali.data.model.UserDto
import com.example.mazali.data.network.RetrofitClient
import com.example.mazali.data.repository.AuthRepository
import com.example.mazali.ui.auth.viewmodel.AuthViewModel
import com.example.mazali.ui.auth.viewmodel.AuthState
import com.example.mazali.ui.auth.viewmodel.AuthViewModelFactory
import com.example.mazali.utils.PrefsManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class TahrirlashFragment : Fragment() {

    lateinit var view__: View
    lateinit var loadingOverlay: ConstraintLayout
    private lateinit var prefs: SharedPreferences
    private lateinit var editName: EditText
    private lateinit var avatarView: ImageView
    private lateinit var userPhone: EditText
    private lateinit var password1: EditText
    private lateinit var password2: EditText
    private lateinit var editBtn: MaterialButton
    private val viewModel: AuthViewModel by viewModels {
        val repository = AuthRepository(RetrofitClient.authApi)
        AuthViewModelFactory(repository, requireContext())
    }

    private var avatarCode: String = "0" // Default avatar

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view__ = inflater.inflate(R.layout.fragment_tahrirlash, container, false)

        // --- Views ---
        loadingOverlay = view__.findViewById(R.id.loadingOverlay_edit)
        editName = view__.findViewById(R.id.user_fulname)
        avatarView = view__.findViewById(R.id.avatar)
        editBtn = view__.findViewById(R.id.edit_btn)
        userPhone = view__.findViewById(R.id.user_phone)

        password1 = view__.findViewById(R.id.password1)
        password2 = view__.findViewById(R.id.password2)

        activity?.let { prefs = it.getSharedPreferences("user_data", Context.MODE_PRIVATE) }

        // --- Prefilled data ---
        editName.setText(PrefsManager.getUserName(requireContext()))
        avatarCode = prefs.getString("avatar", "0") ?: "0"

        userPhone.setText(PrefsManager.getUserPhone(requireContext()))

        setAvatarImage(avatarCode)

        // --- ViewModel ---

        // --- Back button ---
        view__.findViewById<ImageView>(R.id.back_btn).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, ProfilFragment())
                ?.commit()
        }

        // --- Avatar BottomSheet ---
        val bottomsheet = layoutInflater.inflate(R.layout.avatar_layout, null)
        val dialog = context?.let { BottomSheetDialog(it) }
        dialog?.setContentView(bottomsheet)

        avatarView.setOnClickListener { dialog?.show() }

        bottomsheet.findViewById<ImageView>(R.id.item_empity).setOnClickListener {
            avatarView.setImageDrawable(null)
            avatarCode = "0"
            prefs.edit().putString("avatar", avatarCode).apply()
            dialog?.dismiss()
        }

        bottomsheet.findViewById<ImageView>(R.id.item_man).setOnClickListener {
            avatarView.setImageResource(R.drawable.man)
            avatarCode = "1"
            prefs.edit().putString("avatar", avatarCode).apply()
            dialog?.dismiss()
        }

        bottomsheet.findViewById<ImageView>(R.id.item_woman).setOnClickListener {
            avatarView.setImageResource(R.drawable.woman)
            avatarCode = "2"
            prefs.edit().putString("avatar", avatarCode).apply()
            dialog?.dismiss()
        }

        // --- Edit button click ---
        editBtn.setOnClickListener {
            val name = editName.text.toString()
            val phone_num = userPhone.text.toString()
            val password1 = password1.text.toString()
            val password2 = password2.toString()

            loadingOverlay.visibility = View.VISIBLE

            viewModel.updateUser(name = name, phone = phone_num, password = password1 )
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
                        PrefsManager.saveUserData(requireContext(), editName.text.toString(), userPhone.text.toString())
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthState.Error -> {
                        loadingOverlay.visibility = View.GONE
                        Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view__
    }

    private fun setAvatarImage(code: String) {
        when (code) {
            "0" -> avatarView.setImageDrawable(null)
            "1" -> avatarView.setImageResource(R.drawable.man)
            "2" -> avatarView.setImageResource(R.drawable.woman)
            else -> avatarView.setImageDrawable(null)
        }
    }
}
