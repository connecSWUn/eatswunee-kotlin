package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class searchedMenus {
    @SerializedName("menuId")
    @Expose
    var menuId: Long = 0

    @SerializedName("menuName")
    @Expose
    var menuName: String? = null

    @SerializedName("menuRating")
    @Expose
    var menuRating = 0f

    @SerializedName("menuPrice")
    @Expose
    var menuPrice = 0

    @SerializedName("RestaurantName")
    @Expose
    var restaurantName: String? = null
}