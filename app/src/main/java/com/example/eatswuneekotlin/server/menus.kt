package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class menus {
    @SerializedName("menuId")
    @Expose
    var menuId: Long = 0

    @SerializedName("restaurantName")
    @Expose
    var restaurantName: String? = null

    @SerializedName("menuImg")
    @Expose
    var menuImg: String? = null

    @SerializedName("menuName")
    @Expose
    var menuName: String? = null

    @SerializedName("menuRating")
    @Expose
    var menuRating = 0f

    @SerializedName("menuPrice")
    @Expose
    var menuPrice: String? = null

    /* 주문 내역 조회 : 메뉴 */
    @SerializedName("menu_name")
    @Expose
    var menu_name: String? = null

    @SerializedName("menu_price")
    @Expose
    var menu_price = 0

    @SerializedName("menu_cnt")
    @Expose
    var menu_cnt = 0

    @SerializedName("menu_total_price")
    @Expose
    var menu_total_price = 0

    /* 마이페이지 주문 목록 조회 */
    @SerializedName("order_created_at")
    var order_created_at: String? = null

    @SerializedName("order_menu_id")
    var order_menu_id: Long = 0

    @SerializedName("restaurant_name")
    var restaurant_name: String? = null

    @SerializedName("is_user_write_review")
    var is_user_write_review: Boolean? = null
}