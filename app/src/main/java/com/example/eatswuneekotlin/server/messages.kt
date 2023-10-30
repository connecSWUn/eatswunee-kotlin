package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class messages(
    @field:Expose @field:SerializedName("message_created_at") var message_created_at: String,
    @field:Expose @field:SerializedName(
        "message_sender"
    ) var message_sender: String,
    @field:Expose @field:SerializedName("message_content") var message_content: String,
    @field:Expose @field:SerializedName(
        "message_is_read"
    ) var isMessage_is_read: Boolean
)