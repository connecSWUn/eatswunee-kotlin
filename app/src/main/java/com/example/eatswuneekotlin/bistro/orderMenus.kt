package com.example.eatswuneekotlin.bistro

import com.google.gson.annotations.SerializedName

data class orderMenus(
    @SerializedName("orderMenus") private val orderMenus: MutableList<order_menu>
    )
