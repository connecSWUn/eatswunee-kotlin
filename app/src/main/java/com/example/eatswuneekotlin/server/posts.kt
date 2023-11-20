package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class posts (
    @SerializedName("postId") val postId: Long,
    @SerializedName("postTitle") val postTitle: String,
    @SerializedName("postStartTime") val postStartTime: String,
    @SerializedName("postEndTime") val postEndTime: String,
    @SerializedName("postCreatedAt") val postCreatedAt: String,
    @SerializedName("postRecruitStatus") val postRecruitStatus: String,
    @SerializedName("postSpot") val postSpot: String
)