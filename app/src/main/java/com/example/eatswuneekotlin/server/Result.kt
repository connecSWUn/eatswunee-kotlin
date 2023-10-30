package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("post")
    @Expose
    var postList: List<Post>? = null

    /* 게시글 등록 API */
    /* 게시글 등록 API */
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("recruitStatus")
    @Expose
    var recruitStatus: String? = null

    @SerializedName("start_time")
    @Expose
    var start_time: String? = null

    @SerializedName("end_time")
    @Expose
    var end_time: String? = null

    @SerializedName("content")
    @Expose
    var content: String? = null

    @SerializedName("writer_id")
    @Expose
    var writer_id: Long = 0

    /* 작성 글 조회 API */
    @SerializedName("post_total_cnt")
    @Expose
    var post_total_cnt = 0
}