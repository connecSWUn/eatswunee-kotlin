package com.example.eatswuneekotlin.bistro

import com.example.eatswuneekotlin.bistro.recyclerView.orderMenu
import com.google.gson.annotations.SerializedName

data class orderMenuList(
    @SerializedName("orderMenuList") private val orderMenuList: MutableList<orderMenu>
    )
