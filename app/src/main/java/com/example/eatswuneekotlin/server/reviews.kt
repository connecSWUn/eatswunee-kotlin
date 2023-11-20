package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class reviews (
    /* 메뉴 리뷰 조회 */
    @SerializedName("reviewId") val reviewId: Long,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("reviewContent") val reviewContent: String,
    @SerializedName("menuRating") val menuRating: Double,
    @SerializedName("reviewImgs") val reviewImgsList: List<String>,
    @SerializedName("writer") val writer: writer,

    /* 내 리뷰 보기 */
    /* 내가 작성한 리뷰 조회 */
    @SerializedName("restaurant_name") val restaurant_name: String,
    @SerializedName("menu_name") val menu_name: String,
    @SerializedName("review_image_url") val review_image_url: String,
    @SerializedName("menu_review_rating") val menu_review_rating: Double,
    @SerializedName("review_content") val review_content: String,
    @SerializedName("review_created_at") val review_created_at: String,
)