package com.example.eatswuneekotlin.server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class writer {
    @SerializedName("user_id")
    @Expose
    var user_id: String? = null

    @SerializedName("user_name")
    @Expose
    var user_name: String? = null

    @SerializedName("user_profile_url")
    @Expose
    var user_profile_url: String? = null

    @SerializedName("userId")
    @Expose
    var userId: Long = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("profileUrl")
    @Expose
    var profileUrl: String? = null
}