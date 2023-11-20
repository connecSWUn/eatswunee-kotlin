package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class menus (
    @SerializedName("menuId") val menuId: Long,
    @SerializedName("restaurantName") val restaurantName: String,
    @SerializedName("menuImg") val menuImg: String,
    @SerializedName("menuName") val menuName: String,
    @SerializedName("menuRating") val menuRating: String,
    @SerializedName("menuPrice") val menuPrice: String,

    /* 주문 내역 조회 : 메뉴 */
    @SerializedName("menu_name") val menu_name: String,
    @SerializedName("menu_price") val menu_price: Int,
    @SerializedName("menu_cnt") val menu_cnt: Int,
    @SerializedName("menu_total_price") val menu_total_price: Int,

    /* 마이페이지 주문 목록 조회 */
    @SerializedName("order_created_at") val order_created_at: String,
    @SerializedName("order_menu_id") val order_menu_id: Long,
    @SerializedName("restaurant_name") val restaurant_name: String,
    @SerializedName("is_user_write_review") val is_user_write_review: Boolean
)