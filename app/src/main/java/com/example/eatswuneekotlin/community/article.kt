package com.example.eatswuneekotlin.community

import com.google.gson.annotations.SerializedName

data class article (
    @SerializedName("title") private val title: String,
    @SerializedName("recruitStatus") private val recruitStatus: String,
    @SerializedName("start_time") private val start_time: String,
    @SerializedName("end_time") private val end_time: String,
    @SerializedName("recruit_spot") private val recruit_spot: String,
    @SerializedName("content") private val content: String
)