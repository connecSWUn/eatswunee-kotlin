package com.example.eatswuneekotlin.bistro

import com.google.gson.annotations.SerializedName

data class order_menu (
    @SerializedName("menuId") private val menuId: Long,
    @SerializedName("menuCnt") private val menuCnt: Int,
)