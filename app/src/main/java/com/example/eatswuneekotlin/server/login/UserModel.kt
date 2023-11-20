package com.example.eatswuneekotlin.server.login

/** 백엔드로 전송할 데이터 클래스 **/
data class UserModel (
    var loginId : String? = null,
    var password : String? = null
)