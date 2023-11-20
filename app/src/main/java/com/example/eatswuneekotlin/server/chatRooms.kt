package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class chatRooms (
    @SerializedName("chatRoom") val chatRoom: Long,
    @SerializedName("recruitTitle") val recruitTitle: String,
    @SerializedName("senderNickname") val senderNickname: String,
    @SerializedName("recruitId") val recruitId: Long,
    @SerializedName("senderProfileImgUrl") val senderProfileImgUrl: String,
    @SerializedName("lastChatCreatedAt") val lastChatCreatedAt: String,
    @SerializedName("lastChatMessage") val lastChatMessage: String
)