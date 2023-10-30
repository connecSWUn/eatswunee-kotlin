package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class chatRooms {
    @SerializedName("chatRoom")
    @Expose
    var chatRoom: Long = 0

    @SerializedName("recruitTitle")
    @Expose
    lateinit var recruitTitle: String

    @SerializedName("senderNickname")
    @Expose
    lateinit var senderNickname: String

    @SerializedName("recruitId")
    @Expose
    var recruitId: Long = 0

    @SerializedName("senderProfileImgUrl")
    @Expose
    lateinit var senderProfileImgUrl: String

    @SerializedName("lastChatCreatedAt")
    @Expose
    lateinit var lastChatCreatedAt: String

    @SerializedName("lastChatMessage")
    @Expose
    lateinit var lastChatMessage: String
}