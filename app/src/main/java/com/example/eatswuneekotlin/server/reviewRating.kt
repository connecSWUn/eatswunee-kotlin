package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class reviewRating (
    @SerializedName("score5Cnt") val score5Cnt: Long,
    @SerializedName("score4Cnt") val score4Cnt: Long,
    @SerializedName("score3Cnt") val score3Cnt: Long,
    @SerializedName("score2Cnt") val score2Cnt: Long,
    @SerializedName("score1Cnt") val score1Cnt: Long,
)