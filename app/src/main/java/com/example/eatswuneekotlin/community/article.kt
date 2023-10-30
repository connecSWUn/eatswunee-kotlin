package com.example.eatswuneekotlin.community

import com.google.gson.annotations.SerializedName

class article(
    @field:SerializedName("title") private val title: String,
    @field:SerializedName(
        "recruitStatus"
    ) private val recruitStatus: String,
    @field:SerializedName("start_time") private val start_time: String,
    @field:SerializedName(
        "end_time"
    ) private val end_time: String,
    @field:SerializedName("recruit_spot") private val recruit_spot: String?,
    @field:SerializedName(
        "content"
    ) private val content: String
)