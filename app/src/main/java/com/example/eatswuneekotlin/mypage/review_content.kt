package com.example.eatswuneekotlin.mypage

import com.google.gson.annotations.SerializedName

class review_content(
    @field:SerializedName("order_menu_id")
    private val order_menu_id: Long,
    @field:SerializedName(
        "menu_review_rating"
    ) private val menu_review_rating: Double,
    @field:SerializedName(
        "review_image_url"
    ) private val review_image_url: String,
    @field:SerializedName(
        "review_content"
    ) private val review_content: String
)