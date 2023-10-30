package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class posts {
    @SerializedName("postId")
    @Expose
    var postId: Long = 0

    @SerializedName("postTitle")
    @Expose
    var postTitle: String? = null

    @SerializedName("postStartTime")
    @Expose
    var postStartTime: String? = null

    @SerializedName("postEndTime")
    @Expose
    var postEndTime: String? = null

    @SerializedName("postCreatedAt")
    @Expose
    var postCreatedAt: String? = null

    @SerializedName("postRecruitStatus")
    @Expose
    var postRecruitStatus: String? = null

    @SerializedName("postSpot")
    @Expose
    var postSpot: String? = null
}