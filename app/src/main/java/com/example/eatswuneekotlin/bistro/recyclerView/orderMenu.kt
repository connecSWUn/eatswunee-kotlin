package com.example.eatswuneekotlin.bistro.recyclerView

import com.google.gson.annotations.SerializedName

data class orderMenu (
    @SerializedName("menuId") private val menuId: Long,
    @SerializedName("menuCnt") private val menuCnt: Int,
)