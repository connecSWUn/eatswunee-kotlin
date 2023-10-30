package com.example.eatswuneekotlin.server.login

data class Token(
    val accessToken : String,
    val refreshToken : String,
)
