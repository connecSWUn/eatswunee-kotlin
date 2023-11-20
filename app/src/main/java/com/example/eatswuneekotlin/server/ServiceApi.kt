package com.example.eatswuneekotlin.server

import com.example.eatswuneekotlin.bistro.orderMenus
import com.example.eatswuneekotlin.community.article
import com.example.eatswuneekotlin.community.article_status
import com.example.eatswuneekotlin.mypage.review_content
import com.example.eatswuneekotlin.server.login.UserModel
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface ServiceApi {
    /* Get 방식 - @GET(URI) */
    @GET("/recruit/list/{category}")
    fun getData(@Path("category") category: String): Call<Result>

    @GET("/menu/{menuId}")
    fun getData(@Path("menuId") menuId: Long): Call<Result>

    /* 리뷰 불러오기 */
    @GET("{page}/{object}/{userId}")
    fun getData(
        @Path("page") page: String,
        @Path("object") `object`: String,
        @Path("userId") userId: Long
    ): Call<Result>

    @GET("/{path}/{id}")
    fun getData(@Path("path") path: String, @Path("id") id: Long): Call<Result>

    @GET("/gusia/{restaurantId}")
    fun getMenuList(@Path("restaurantId") restaurantId: Int): Call<Result>

    /* 메뉴 검색 API */
    @GET("/gusia/search/{restaurantId}/{keyword}")
    fun getData(
        @Path("restaurantId") restaurantId: Long,
        @Path("keyword") keyword: String
    ): Call<Result>

    /* 마이페이지 화면 조회 */
    @GET("/mypage")
    fun getProfile(): Call<Result>

    /* 프로필 이미지 업로드 */
    @POST("/user/profile")
    fun uploadProfile(@Body image: File): Call<Result>

    /* 닉네임 중복 확인 */
    @GET("/mypage/duplicated/{nickname}")
    fun changeNickname(@Path("nickname") nickname: String): Call<Result>

    /* 작성 글 목록 조회 */
    @GET("/recruit/writer/{recruitStatus}")
    fun getArticles(@Path("recruitStatus") recruitStatus: String): Call<Result>

    /* 마이페이지 주문 목록 조회 */
    @GET("/mypage/orders")
    fun getOrderList(): Call<Result>

    /* 마이페이지 리뷰 목록 조회 */
    @GET("/mypage/reviews")
    fun getReviews(): Call<Result>

    /* 채팅방 존재 여부 확인 */
    @GET("/chat/exist/{recruitId}")
    fun getExist(@Path("recruitId") recruitId: Long): Call<Result>

    /* 채팅방 입장 */
    @GET("/chat/enter/{chatRoomId}")
    fun enterChat(@Path("chatRoomId") chatRoomId: Long): Call<Result>

    /* 채팅방 만들기 */
    @GET("/chat/create/{recruitId}")
    fun makeChat(@Path("recruitId") recruitId: Long): Call<Result>

    @GET("/user/chatroom/list")
    fun getChatList(): Call<Result>

    @GET("/gusia/search/{restaurantId}/{keyword}")
    fun getSearch(
        @Path("restaurantId") restaurantId: Long,
        @Path("keyword") keyword: String
    ): Call<Result>

    /* 게시글 삭제 */
    @DELETE("/recruit/delete/{postId}")
    fun postDelete(@Path("postId") postId: Long): Call<Result>

    /* 게시글 상태 수정 */
    @POST("/recruit/edit")
    fun editStatus(@Body articleStatus: article_status): Call<Result>

    /* 리뷰 */
    @DELETE("/mypage/review/delete/{reviewId}")
    fun reviewDelete(@Path("reviewId") reviewId: Long): Call<Result>

    @DELETE("/mypage/review/save")
    fun postReview(@Body review_content: review_content): Call<Result>

    /* 로그인 */
    @POST("/login/user")
    fun userLogin(@Body userModel: UserModel): Call<Result>

    /* 회원가입 */
    @POST("/signup/user")
    fun postRegister(@Body accountRegisterDto: AccountRegisterDto): Call<Result>

    @GET("mypage/duplicated/loginId/{loginId}")
    fun isIdDuplicated(@Path("loginId") loginId: String): Call<Result>

    @GET("mypage/duplicated/{nickname}")
    fun isNicknameDuplicated(@Path("nickname") nickname: String): Call<Result>

    /* 주문 */
    @POST("/gusia/order/save")
    fun postOrder(@Body orderMenus: orderMenus): Call<Result>

    @POST("/recruit/save")
    fun postArticle(@Body article: article): Call<Result>
}