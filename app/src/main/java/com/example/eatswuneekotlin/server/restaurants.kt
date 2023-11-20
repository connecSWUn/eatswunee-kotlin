package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class restaurants (
    @SerializedName("restaurantId") val restaurantId: Int,
    @SerializedName("restaurantName") val restaurantName: String
)