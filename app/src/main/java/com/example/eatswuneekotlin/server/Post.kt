package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Post {
    @SerializedName("recruitId")
    @Expose
    var recruitId: Long? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("recruitStatus")
    @Expose
    var recruitStatus: String? = null

    @SerializedName("spot")
    @Expose
    var spot: String? = null

    @SerializedName("startTime")
    @Expose
    var startTime: String? = null

    @SerializedName("endTime")
    @Expose
    var endTime: String? = null
}