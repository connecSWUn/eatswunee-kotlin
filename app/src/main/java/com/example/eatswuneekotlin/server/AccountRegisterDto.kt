package com.example.eatswuneekotlin.server

import com.google.gson.annotations.SerializedName

data class AccountRegisterDto(
    @SerializedName("loginId") val loginId: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("password") val password: String
)