package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class homeOrders (
    @SerializedName("orderId") val orderId: Long,
    @SerializedName("orderNum") val orderNum: Int,
    @SerializedName("restaurantId") val restaurantId: Int,
    @SerializedName("expectedWaitingTime") val expectedWaitingTime: Int
)