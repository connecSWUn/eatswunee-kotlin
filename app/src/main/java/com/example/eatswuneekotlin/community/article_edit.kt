package com.example.eatswuneekotlin.community

import com.google.gson.annotations.SerializedName

data class article_edit (
    @SerializedName("recruitId") val recruitId: Long,
    @SerializedName("recruitContent") val recruit_content: String,
)