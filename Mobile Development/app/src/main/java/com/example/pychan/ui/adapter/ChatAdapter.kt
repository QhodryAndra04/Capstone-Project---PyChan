package com.example.pychan.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pychan.R

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val messages = mutableListOf<Pair<String, Boolean>>()

    fun addMessage(message: String, isUser: Boolean) {
        messages.add(Pair(message, isUser))
        notifyItemInserted(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_bubble, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val (message, isUser) = messages[position]
        holder.bind(message, isUser)
    }

    override fun getItemCount(): Int = messages.size

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderBubble: LinearLayout = itemView.findViewById(R.id.senderBubble)
        private val receiverBubble: LinearLayout = itemView.findViewById(R.id.receiverBubble)
        private val senderMessageTextView: TextView = itemView.findViewById(R.id.tvSenderMessage)
        private val receiverMessageTextView: TextView =
            itemView.findViewById(R.id.tvReceiverMessage)

        fun bind(message: String, isUser: Boolean) {
            if (isUser) {
                senderBubble.visibility = View.VISIBLE
                receiverBubble.visibility = View.GONE
                senderMessageTextView.text = message
            } else {
                senderBubble.visibility = View.GONE
                receiverBubble.visibility = View.VISIBLE
                receiverMessageTextView.text = message
            }
        }
    }
}