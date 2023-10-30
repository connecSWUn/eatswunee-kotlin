package com.example.eatswuneekotlin.server.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eatswuneekotlin.R
import com.example.eatswuneekotlin.server.messages

class chatAdapter(
    private val user_name: String?,
    private val messagesList: MutableList<messages?>?,
    var context: Context

) : RecyclerView.Adapter<chatAdapter.ViewHolder>() {

    val TYPE_MY = 0
    val TYPE_OTHER = 1

    override fun getItemViewType(position: Int): Int {
        return if (messagesList!![position]?.message_sender == user_name) {
            TYPE_OTHER
        } else {
            TYPE_MY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = if (viewType == TYPE_MY) LayoutInflater.from(context)
            .inflate(R.layout.chat_my_message, parent, false) else LayoutInflater.from(
            context
        ).inflate(R.layout.chat_other_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = messagesList!![position]
        holder.message.text = item?.message_content
        holder.date.text = item?.message_created_at
    }

    override fun getItemCount(): Int {
        return messagesList!!.size
    }

    fun addChat(messages: messages?) {
        messagesList!!.add(messages)
        notifyItemInserted(messagesList.size - 1)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView
        var date: TextView

        init {
            message = itemView.findViewById(R.id.message)
            date = itemView.findViewById(R.id.message_date)
        }
    }
}