package com.example.eatswuneekotlin.server.login

import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/** Refresh Api 호출을 위한 인터페이스
 * 새로운 access, refresh token 반환
 */
interface TokenRefreshApi : BaseApi {
    @FormUrlEncoded
    @POST("token")
    suspend fun patchToken(
        @Body refreshToken: String?
    ) : Token
}