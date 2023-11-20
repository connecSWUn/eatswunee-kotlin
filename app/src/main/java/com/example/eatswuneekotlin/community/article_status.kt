package com.example.eatswuneekotlin.community

import com.google.gson.annotations.SerializedName

data class article_status(
    @SerializedName("recruitId") private val recruitId: Long,
    @SerializedName("recruitStatus") private val recruitStatus: String
)
