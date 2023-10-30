package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class restaurants {
    @SerializedName("restaurantId")
    @Expose
    var restaurantId = 0

    @SerializedName("restaurantName")
    @Expose
    var restaurantName: String? = null
}