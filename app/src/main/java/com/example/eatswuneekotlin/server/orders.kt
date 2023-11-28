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
    @SerializedName("userWriteReview") val isUserWriteReview: Boolean,

    /* 주문 목록 조회 */
    @SerializedName("restaurant_name") val restaurant_name: String,
    @SerializedName("order_restaurant_waiting_time") val order_restaurant_waiting_time: Int,
    @SerializedName("restaurant_total_price") val restaurant_total_price: Int,
    @SerializedName("menus") val menusList: List<menus>,
)