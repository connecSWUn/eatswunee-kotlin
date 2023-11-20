package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class messages(
    @SerializedName("message_created_at") val message_created_at: String,
    @SerializedName("message_sender") val message_sender: String,
    @SerializedName("message_content") val message_content: String,
    @SerializedName("message_is_read") val isMessage_is_read: Boolean
)