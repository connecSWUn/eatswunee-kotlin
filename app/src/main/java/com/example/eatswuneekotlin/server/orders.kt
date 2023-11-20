package com.example.eatswuneekotlin.server

import com.google.gson.annotations.SerializedName

data class orders (
    /* 마이페이지 주문 목록 조회 */
    @SerializedName("orderMenuId") val orderMenuId: Long,
    @SerializedName("orderCreatedAt") val orderCreatedAt: String,
    @SerializedName("restaurantName") val restaurantName: String,
    @SerializedName("menuName") val menuName: String,
    @SerializedName("menuTotalPrice") val menuTotalPrice: Int,
    @SerializedName("menuPrice") val menuPrice: Int,
    @SerializedName("menuCnt") val menuCnt: Int,
    @SerializedName("userWriteReview") val isUserWriteReview: Boolean
)