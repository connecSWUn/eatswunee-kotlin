package com.example.eatswuneekotlin.server.login

/** 백엔드로 전송할 데이터 클래스 **/
data class UserModel (
    var id : String? = null,
    var pw : String? = null
)

/** 백엔드에서 받는 데이터 클래스 **/
data class LoginBackendResponse (
    val code : String,

    // 200: 성공, 300-400: 에러
    val message : String,
    val token : String
)
