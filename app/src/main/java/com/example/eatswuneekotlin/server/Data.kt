package com.example.eatswuneekotlin.server

import com.google.gson.annotations.SerializedName

data class Data (
    /* 로그인 */
    @SerializedName("grantType") val grantType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,

    @SerializedName("category") val category: String,
    @SerializedName("cursorId") val cursorId: String,
    @SerializedName("post") val postList: List<Post>,
    @SerializedName("post_id") val post_id: Long,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("title") val title: String,
    @SerializedName("spot") val spot: String,
    @SerializedName("start_time") val start_time: String,
    @SerializedName("end_time") val end_time: String,
    @SerializedName("recruit_status") val recruit_status: String,
    @SerializedName("writer") val writers: writer,
    @SerializedName("content") val content: String,
    @SerializedName("user_is_writer") val isUser_is_writer: Boolean,

    /* 식당별 메뉴 조회 */
    /* 식당별 메뉴 리스트 조회 API */
    @SerializedName("homeOrders") val homeOrdersList: List<homeOrders>,
    @SerializedName("menus") val menusList: List<menus>,
    @SerializedName("restaurants") val restaurantsList: List<restaurants>,

    /* 메뉴 내용 조회 */
    /* 메뉴 내용 조회 API */
    @SerializedName("menuId") val menuId: Long,
    @SerializedName("RestaurantName") val restaurantName: String,
    @SerializedName("menuName") val menuName: String,
    @SerializedName("menuPrice") val menuPrice: Int,
    @SerializedName("menuRating") val menuRating: Double,
    @SerializedName("menuReviewCnt") val menuReviewCnt: Int,

    /* 메뉴 검색 API */
    /* 메뉴 검색 API */
    @SerializedName("searchedMenus") val searchedMenusList: List<searchedMenus>,

    /* 메뉴 리뷰 조회 */
    /* 메뉴의 리뷰 조회 */
    @SerializedName("reviewCnt") val reviewCnt: Long,
    @SerializedName("menuImg") val menuImg: String,
    @SerializedName("menuAvgRating") val menuAvgRating: Double,
    @SerializedName("reviewRating") val reviewRating: reviewRating,
    @SerializedName("reviews") val reviewsList: List<reviews>,

    /* 주문 내역 조회 */
    @SerializedName("orders") val ordersList: List<orders>,
    @SerializedName("order_num") val order_num: String,
    @SerializedName("order_created_at") val order_created_at: String,
    @SerializedName("order_total_price") val order_total_price: String,

    /* 마이페이지 화면 조회 */
    @SerializedName("user_id") val user_id: Long,
    @SerializedName("user_profile_url") val user_profile_url: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("loginId") val loginId: String,

    /* 작성 게시물 조회 */
    @SerializedName("userId") val userId: Long,
    @SerializedName("userName") val userName: String,
    @SerializedName("postTotalCnt") val postTotalCnt: Int,
    @SerializedName("posts") val postsList: List<posts>,

    /* 채팅방 존재 여부 확인 */
    @SerializedName("exist_chatroom") val isExist_chatroom: Boolean,

    /* 채팅방 입장 */
    @SerializedName("recruitStatus") val recruitStatus: String,
    @SerializedName("recruit_title") val recruit_title: String,
    @SerializedName("recruit_spot") val recruit_spot: String,
    @SerializedName("recruit_start_time") val recruit_start_time: String,
    @SerializedName("recruit_end_time") val recruit_end_time: String,
    @SerializedName("recruit_created_at") val recruit_created_at: String,
    @SerializedName("sender_name") val sender_name: String,
    @SerializedName("messages") val messagesList: List<messages>,

    /* 채팅방 만들기 */
    @SerializedName("chat_room_id") val chat_room_id: Long,

    /* 채팅방 리스트 */
    @SerializedName("chatRooms") val chatRoomsList: List<chatRooms>,

    /* 중복 확인 */
    @SerializedName("is_duplicated") val isIs_duplicated: Boolean,

    /* 존재하는 채팅방 개수 */
    @SerializedName("chat_room_number") val chat_room_number: String,
)