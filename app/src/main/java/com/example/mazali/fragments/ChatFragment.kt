package com.example.mazali.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.adapters.ChatAdapter
import com.example.mazali.data.model.ChatMessage
import com.example.mazali.data.network.ChatApiService
import com.example.mazali.data.network.RetrofitClient5
import com.example.mazali.data.repository.ChatRepository
import com.example.mazali.ui.auth.viewmodel.ChatViewModel
import com.example.mazali.ui.auth.viewmodel.ChatViewModelFactory

class ChatFragment : Fragment() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton

    private lateinit var viewModel: ChatViewModel
    private lateinit var backBtn: ImageView
    private var isBotResponding = false  // Bot javobi kutilmoqda flag

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)
        backBtn = view.findViewById(R.id.back_btn)

        backBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.user_frame, ProfilFragment())
                ?.commit()
        }

        chatAdapter = ChatAdapter(requireContext(), messages)
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        chatRecyclerView.adapter = chatAdapter

        // ViewModel va Repository integratsiyasi
        val api = RetrofitClient5.instance.create(ChatApiService::class.java)
        val repository = ChatRepository(api)
        val factory = ChatViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

        // Bot javoblarini kuzatish
        viewModel.reply.observe(viewLifecycleOwner) { response ->
            // "..." ni o'chirib, haqiqiy javobni qo'shish
            val lastIndex = messages.indexOfLast { it.isBot && it.text == "..." }
            if (lastIndex != -1) {
                messages[lastIndex] = ChatMessage(response.reply, isBot = true)
                chatAdapter.notifyItemChanged(lastIndex)
                chatRecyclerView.scrollToPosition(messages.size - 1)
            }
            isBotResponding = false
            messageEditText.isEnabled = true
            sendButton.isEnabled = true
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                // User yozishini vaqtincha bloklash
                isBotResponding = true
                messageEditText.isEnabled = false
                sendButton.isEnabled = false

                // Yangi bot xabari sifatida "..." qo'shish
                messages.add(ChatMessage("...", isBot = true))
                chatAdapter.notifyItemInserted(messages.size - 1)
                chatRecyclerView.scrollToPosition(messages.size - 1)
            }
        }

        sendButton.setOnClickListener {
            if (isBotResponding) return@setOnClickListener

            val messageText = messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // User xabarini qo'shish
                messages.add(ChatMessage(messageText, isBot = false))
                chatAdapter.notifyItemInserted(messages.size - 1)
                chatRecyclerView.scrollToPosition(messages.size - 1)

                messageEditText.text.clear()

                // Bot javobini chaqirish
                viewModel.sendMessage(messageText)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }
}
