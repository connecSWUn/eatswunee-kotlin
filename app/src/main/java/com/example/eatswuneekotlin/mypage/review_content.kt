package com.example.eatswuneekotlin.mypage

import com.google.gson.annotations.SerializedName

class review_content(
    @SerializedName("order_menu_id") private val order_menu_id: Long,
    @SerializedName("menu_review_rating") private val menu_review_rating: Double,
    @SerializedName("review_image_url") private val review_image_url: String,
    @SerializedName("review_content") private val review_content: String
)