package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {
    /* 로그인 */
    @SerializedName("grantType")
    var grantType: String? = null

    @SerializedName("accessToken")
    var accessToken: String? = null

    @SerializedName("refreshToken")
    var refreshToken: String? = null

    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("cursorId")
    @Expose
    var cursorId: String? = null

    @SerializedName("post")
    @Expose
    var postList: List<Post>? = null

    @SerializedName("post_id")
    var post_id: Long = 0

    @SerializedName("created_at")
    var created_at: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("spot")
    var spot: String? = null

    @SerializedName("start_time")
    var start_time: String? = null

    @SerializedName("end_time")
    var end_time: String? = null

    @SerializedName("recruit_status")
    var recruit_status: String? = null

    @SerializedName("writer")
    var writers: writer? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("user_is_writer")
    var isUser_is_writer = false

    /* 식당별 메뉴 조회 */
    /* 식당별 메뉴 리스트 조회 API */
    @SerializedName("homeOrders")
    var homeOrdersList: List<homeOrders>? = null

    @SerializedName("menus")
    var menusList: List<menus>? = null

    @SerializedName("restaurants")
    var restaurantsList: List<restaurants>? = null

    /* 메뉴 내용 조회 */
    /* 메뉴 내용 조회 API */
    @SerializedName("menuId")
    @Expose
    var menuId: Long = 0

    @SerializedName("RestaurantName")
    @Expose
    var restaurantName: String? = null

    @SerializedName("menuName")
    @Expose
    var menuName: String? = null

    @SerializedName("menuPrice")
    @Expose
    var menuPrice = 0

    @SerializedName("menuRating")
    @Expose
    var menuRating = 0f

    @SerializedName("menuReviewCnt")
    @Expose
    var menuReviewCnt = 0f

    /* 메뉴 검색 API */
    /* 메뉴 검색 API */
    @SerializedName("searchedMenus")
    @Expose
    var searchedMenusList: List<searchedMenus>? = null

    /* 메뉴 리뷰 조회 */
    /* 메뉴의 리뷰 조회 */
    @SerializedName("reviewCnt")
    @Expose
    var reviewCnt: Long = 0

    @SerializedName("menuImg")
    @Expose
    var menuImg: String? = null

    @SerializedName("menuAvgRating")
    @Expose
    var menuAvgRating = 0f

    @SerializedName("reviewRating")
    var reviewRating: reviewRating? = null

    @SerializedName("reviews")
    var reviewsList: List<reviews>? = null

    /* 주문 내역 조회 */
    @SerializedName("orders")
    var ordersList: List<orders>? = null

    @SerializedName("order_num")
    @Expose
    var order_num: String? = null

    @SerializedName("order_created_at")
    @Expose
    var order_created_at: String? = null

    @SerializedName("order_total_price")
    @Expose
    var order_total_price: String? = null

    /* 마이페이지 화면 조회 */
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null

    @SerializedName("user_profile_url")
    @Expose
    var user_profile_url: String? = null

    @SerializedName("user_name")
    @Expose
    var user_name: String? = null

    @SerializedName("loginId")
    @Expose
    var loginId: String? = null

    /* 작성 게시물 조회 */
    @SerializedName("userId")
    var userId: Long = 0

    @SerializedName("userName")
    var userName: String? = null

    @SerializedName("postTotalCnt")
    var postTotalCnt = 0

    @SerializedName("posts")
    var postsList: List<posts>? = null

    /* 채팅방 존재 여부 확인 */
    @SerializedName("exist_chatroom")
    @Expose
    var isExist_chatroom = false

    /* 채팅방 입장 */
    @SerializedName("recruitStatus")
    @Expose
    var recruitStatus: String? = null

    @SerializedName("recruit_title")
    @Expose
    var recruit_title: String? = null

    @SerializedName("recruit_spot")
    @Expose
    var recruit_spot: String? = null

    @SerializedName("recruit_start_time")
    @Expose
    var recruit_start_time: String? = null

    @SerializedName("recruit_end_time")
    @Expose
    var recruit_end_time: String? = null

    @SerializedName("recruit_created_at")
    @Expose
    var recruit_created_at: String? = null

    @SerializedName("sender_name")
    @Expose
    var sender_name: String? = null

    @SerializedName("messages")
    var messagesList: MutableList<messages>? = null

    /* 채팅방 만들기 */
    @SerializedName("chat_room_id")
    @Expose
    var chat_room_id: Long = 0

    /* 채팅방 리스트 */
    @SerializedName("chatRooms")
    var chatRoomsList: List<chatRooms>? = null

    /* 중복 확인 */
    @SerializedName("is_duplicated")
    var isIs_duplicated = false
        private set

    fun setIs_duplicated(is_duplicated: Boolean) {
        isIs_duplicated = is_duplicated
    }
}