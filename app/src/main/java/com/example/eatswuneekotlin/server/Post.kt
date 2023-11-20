package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Post (
    @SerializedName("recruitId") val recruitId: Long,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("title") val title: String,
    @SerializedName("recruitStatus") val recruitStatus: String,
    @SerializedName("spot") val spot: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String
)