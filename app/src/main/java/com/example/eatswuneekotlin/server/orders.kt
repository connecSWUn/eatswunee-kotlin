package com.example.eatswuneekotlin.server

import com.google.gson.annotations.SerializedName

class orders {
    /* 마이페이지 주문 목록 조회 */
    @SerializedName("orderMenuId")
    var orderMenuId: Long = 0

    @SerializedName("orderCreatedAt")
    var orderCreatedAt: String? = null

    @SerializedName("restaurantName")
    var restaurantName: String? = null

    @SerializedName("menuName")
    var menuName: String? = null

    @SerializedName("menuTotalPrice")
    var menuTotalPrice = 0

    @SerializedName("menuPrice")
    var menuPrice = 0

    @SerializedName("menuCnt")
    var menuCnt = 0

    @SerializedName("userWriteReview")
    var isUserWriteReview = false
}