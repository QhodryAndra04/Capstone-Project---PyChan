package com.example.pychan.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pychan.R
import com.example.pychan.data.ShowResponse
import com.example.pychan.retrofit.RetrofitClient.mlApiService
import com.example.pychan.ui.adapter.ChatAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sendButton: ImageButton
    private lateinit var editTextMessage: EditText
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_consultation, container, false)

        // Initialize UI components
        recyclerView = view.findViewById(R.id.recyclerView)
        sendButton = view.findViewById(R.id.btnSend)
        editTextMessage = view.findViewById(R.id.edit_text_message)

        // Set up RecyclerView
        chatAdapter = ChatAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = chatAdapter

        // Set action for send button
        sendButton.setOnClickListener {
            val message = editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                // Add user's message to the chat
                chatAdapter.addMessage(message, true)

                // Clear the input field
                editTextMessage.text.clear()

                // Send message to the API
                sendMessageToApi(message)
            } else {
                Log.w("ConsultationFragment", "Message is empty. Not sending.")
            }
        }

        return view
    }

    private fun sendMessageToApi(message: String) {
        // Send API request with dynamic input
        mlApiService.showResponse(input = message).enqueue(object : Callback<ShowResponse> {
            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()?.response ?: "No response from API"
                    Log.d("ConsultationFragment", "API Response: $apiResponse")
                    // Add chatbot's response to the chat
                    chatAdapter.addMessage(apiResponse, false)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(
                        "ConsultationFragment",
                        "Error Response: ${response.code()} - $errorBody"
                    )
                    chatAdapter.addMessage(
                        "Error: ${response.code()} - Unable to fetch response",
                        false
                    )
                }
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                Log.e("ConsultationFragment", "API Call Failed: ${t.message}")
                chatAdapter.addMessage("Error: ${t.message}", false)
            }
        })
    }
}