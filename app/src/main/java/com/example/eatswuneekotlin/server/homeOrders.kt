package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class homeOrders {
    @SerializedName("orderId")
    @Expose
    var orderId: Long = 0

    @SerializedName("orderNum")
    @Expose
    var orderNum = 0

    @SerializedName("restaurantId")
    @Expose
    var restaurantId = 0

    @SerializedName("expectedWaitingTime")
    @Expose
    var expectedWaitingTime = 0
}