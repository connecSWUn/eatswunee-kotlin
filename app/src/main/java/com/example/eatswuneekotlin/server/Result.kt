package com.example.eatswuneekotlin.server

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("data") val data: Data,
    @SerializedName("post") val postList: List<Post>,

    /* 게시글 등록 API */
    @SerializedName("title") val title: String,
    @SerializedName("recruitStatus") val recruitStatus: String,
    @SerializedName("start_time") val start_time: String,
    @SerializedName("end_time") val end_time: String,
    @SerializedName("content") val content: String,
    @SerializedName("writer_id") val writer_id: Long,

    /* 작성 글 조회 API */
    @SerializedName("post_total_cnt") val post_total_cnt: Int
)