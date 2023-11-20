package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class writer (
    @SerializedName("user_id") val user_id: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("user_profile_url") val user_profile_url: String,
    @SerializedName("userId") val userId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("profileUrl") val profileUrl: String,
)