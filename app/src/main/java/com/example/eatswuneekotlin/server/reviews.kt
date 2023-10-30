package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class reviews {
    /* 메뉴 리뷰 조회 */
    @SerializedName("reviewId")
    @Expose
    var reviewId: Long = 0

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("reviewContent")
    @Expose
    var reviewContent: String? = null

    @SerializedName("menuRating")
    @Expose
    var menuRating = 0

    @SerializedName("reviewImgs")
    var reviewImgsList: ArrayList<String>? = null

    @SerializedName("writer")
    var writer: writer? = null

    /* 내 리뷰 보기 */
    /* 내가 작성한 리뷰 조회 */
    @SerializedName("restaurant_name")
    @Expose
    var restaurant_name: String? = null

    @SerializedName("menu_name")
    @Expose
    var menu_name: String? = null

    @SerializedName("review_image_url")
    @Expose
    var review_image_url: String? = null

    @SerializedName("menu_review_rating")
    @Expose
    var menu_review_rating = 0.0

    @SerializedName("review_content")
    @Expose
    var review_content: String? = null

    @SerializedName("review_created_at")
    @Expose
    var review_created_at: String? = null
}