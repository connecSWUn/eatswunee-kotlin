package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class searchedMenus (
    @SerializedName("menuId") val menuId: Long,
    @SerializedName("menuName") val menuName: String,
    @SerializedName("menuRating") val menuRating: Double,
    @SerializedName("menuPrice") val menuPrice: Int,
    @SerializedName("RestaurantName") val restaurantName: String,
)