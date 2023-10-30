package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class reviewRating {
    @SerializedName("score5Cnt")
    @Expose
    var score5Cnt: Long = 0

    @SerializedName("score4Cnt")
    @Expose
    var score4Cnt: Long = 0

    @SerializedName("score3Cnt")
    @Expose
    var score3Cnt: Long = 0

    @SerializedName("score2Cnt")
    @Expose
    var score2Cnt: Long = 0

    @SerializedName("score1Cnt")
    @Expose
    var score1Cnt: Long = 0
}