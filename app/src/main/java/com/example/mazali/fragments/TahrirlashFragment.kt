package com.example.mazali.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mazali.R
import com.google.android.material.button.MaterialButton

class TahrirlashFragment : Fragment() {
    lateinit var view__: View

    private lateinit var prefs: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view__ =  inflater.inflate(R.layout.fragment_tahrirlash, container, false)

        view__.findViewById<ImageView>(R.id.back_btn).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, ProfilFragment())
                ?.commit()
        }

        activity?.let { prefs = it.getSharedPreferences("user_data", Context.MODE_PRIVATE) }
        val editor = prefs.edit()

        try {
            if (prefs.getString("avatar","0").toString().hashCode() == "0".hashCode()){
                view__.findViewById<ImageView>(R.id.avatar).setImageDrawable(null)
            }else if(prefs.getString("avatar","0").toString().hashCode() == "1".hashCode()){
                view__.findViewById<ImageView>(R.id.avatar).setImageResource(R.drawable.man)
            }else if(prefs.getString("avatar","0").toString().hashCode() == "2".hashCode()){
                view__.findViewById<ImageView>(R.id.avatar).setImageResource(R.drawable.woman)
            }
        } catch (e: Exception){
            view__.findViewById<ImageView>(R.id.avatar).setImageDrawable(null)
        }

        val bottomsheet = layoutInflater.inflate(R.layout.avatar_layout, null)
        val dialog = context?.let { com.google.android.material.bottomsheet.BottomSheetDialog(it) }
        dialog?.setContentView(bottomsheet)

        view__.findViewById<ImageView>(R.id.avatar).setOnClickListener {
            dialog?.show()
        }

        val avatarView = view__.findViewById<ImageView>(R.id.avatar)

        bottomsheet.findViewById<ImageView>(R.id.item_empity).setOnClickListener {
            avatarView.setImageDrawable(null)
            editor.putString("avatar", "0")
            editor.commit()
            dialog?.dismiss()
        }

        bottomsheet.findViewById<ImageView>(R.id.item_man).setOnClickListener {
            avatarView.setImageResource(R.drawable.man)
            editor.putString("avatar", "1")
            editor.commit()
            dialog?.dismiss()
        }

        bottomsheet.findViewById<ImageView>(R.id.item_woman).setOnClickListener {
            avatarView.setImageResource(R.drawable.woman)
            editor.putString("avatar", "2")
            editor.commit()
            dialog?.dismiss()
        }

        view__.findViewById<MaterialButton>(R.id.edit_btn).setOnClickListener {
            editor.putString("user_name",view__.findViewById<EditText>(R.id.user_fulname).text.toString())
            editor.commit()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, ProfilFragment())
                ?.commit()
            Toast.makeText(context, "Ma'lumotlar yangilandi", Toast.LENGTH_SHORT).show()
        }

        return view__
    }

}