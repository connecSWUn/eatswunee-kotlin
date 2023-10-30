package com.example.eatswuneekotlin.server

import com.google.gson.annotations.SerializedName

class AccountRegisterDto(
    @field:SerializedName("loginId") private val loginId: String,
    @field:SerializedName(
        "nickname"
    ) private val nickname: String,
    @field:SerializedName("password") private val password: String
)